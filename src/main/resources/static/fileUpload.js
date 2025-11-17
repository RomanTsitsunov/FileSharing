document.getElementById('authorizationForm').addEventListener('submit', event => login(event));
document.getElementById('logoutButton').addEventListener('click', event => logout());
document.getElementById('fileUploadForm').addEventListener('submit', event => uploadFile(event));
localStorage.clear()
loadFiles();

async function login(event) {
    event.preventDefault();
    const login = document.getElementById('loginInput').value;
    const pass = document.getElementById('passInput').value;
    const response = await fetch('/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ login, pass })
    });
    if (response.ok) {
        const result = await response.json();
        localStorage.setItem('login', result.login);
        document.getElementById('authorizationInfo').innerHTML = `Вошел в систему как: ${result.login}`;
        document.getElementById('authorizationForm').setAttribute('hidden', '');
        document.getElementById('logoutButton').removeAttribute('hidden');
    } else if(response.status === 403) {
        alert('Неверный логин или пароль');
    }
}

function logout() {
    localStorage.removeItem('login');
    document.getElementById('authorizationInfo').innerHTML = 'Не авторизован';
    document.getElementById('authorizationForm').removeAttribute('hidden');
    document.getElementById('logoutButton').setAttribute('hidden', '');
}

async function uploadFile(event) {
    event.preventDefault();
    const progressContainer = document.getElementById('progressContainer');
    const progress = document.getElementById('progressBar');
    const progressStatus = document.getElementById('progressStatus');
    const file = document.getElementById('fileInput').files[0];
    if (!file) {
        alert("Выберите файл для загрузки");
        return;
    }
    if (!localStorage.getItem('login')) {
        alert("Только авторизованный пользователь может загружать файлы");
        return;
    }
    const formData = new FormData();
    formData.append('file', file);
    
    const xhr = new XMLHttpRequest();
    xhr.responseType = 'json';
    xhr.upload.addEventListener('progress', event => {
        if (event.lengthComputable) {
            const percent = Math.round((event.loaded / event.total) * 100);
            progressContainer.removeAttribute('hidden');
            progress.value = percent;
            progressStatus.textContent = percent + '%';
        }
    });
    xhr.addEventListener('load', function () {  
        if (xhr.status === 200) {
            progressContainer.setAttribute('hidden', '');
            const filesList = document.getElementById('filesList');
            const result = xhr.response;
            const listItem = document.getElementById(result.file.name);
            if (listItem) {
                updateListItem(listItem, result.file)
            } else {
                filesList.append(createListItem(result.file));
            }
        
        }
    });  
    xhr.open('POST', '/uploadfile');
    xhr.send(formData);
}

async function loadFiles() {
    const filesList = document.getElementById('filesList');
    const response = await fetch('/loadfiles', {
        method: 'GET'
    });
    if (response.ok) {
        const result = await response.json();
        result.files.forEach(file => {
            filesList.append(createListItem(file));
        });
    }
}

function updateListItem(listItem, file) {
    const lastDownloadDate = file.lastDownloadDate === null ? '' : new Date(file.lastDownloadDate)
    listItem.innerHTML = `
        <div>
            <span>Информация о файле:</span></br>
            <span>Имя: ${file.name}</span></br>
            <span>Размер: ${file.size} byte</span></br>
            <span>Дата загрузки: ${new Date(file.uploadDate)}</span></br>
            <span>Дата последнего скачивания: ${lastDownloadDate}</span></br>
            <span>Количество скачиваний: ${file.downloadCount}</span></br>
            <a href="${file.downloadLink}">Скачать</a>
        </div>
    `;
}

function createListItem(file) {
    const listItem = document.createElement('li');
    listItem.id = file.name;
    const lastDownloadDate = file.lastDownloadDate === null ? '' : new Date(file.lastDownloadDate);
    let downloadCount = file.downloadCount;
    listItem.innerHTML = `
        <div>
            <span>Информация о файле:</span></br>
            <span>Имя: ${file.name}</span></br>
            <span>Размер: ${file.size} byte</span></br>
            <span>Дата загрузки: ${new Date(file.uploadDate)}</span></br>
            <span class="lastDownloadDate">Дата последнего скачивания: ${lastDownloadDate}</span><br>
            <span class="downloadCount">Количество скачиваний: ${downloadCount}</span><br>
            <a class="downloadLink" href="${file.downloadLink}">Скачать</a>
        </div>
    `;

    listItem.querySelector('.downloadLink').addEventListener('click', async (event) => {
            setTimeout(() => {
                downloadCount += 1;
                listItem.querySelector('.lastDownloadDate').textContent = `Дата последнего скачивания: ${new Date()}`;
                listItem.querySelector('.downloadCount').textContent = `Количество скачиваний: ${downloadCount}`;
            }, 200);
        });

    return listItem;
}