const snackBars = document.querySelector('#snackBars');
const listContainer = document.querySelector('#service-list');
const servicesRequest = new Request('http://localhost:8080/service');
const saveButton = document.querySelector('#post-service');
const deleteButton = document.querySelector('#delete-service');
const urlName = document.getElementById('url-name');
const serviceName = document.getElementById('service-name');

function getStatus(service) {
    return service.alive === 'PENDING' ? service.alive : service.alive ? 'ALIVE' : 'DEAD';
}

function serviceToString(service) {
    return service.name + ': ' + service.url + ' | Status : ' + getStatus(service);
}

function createServiceElement(li, service, animated) {
    li.appendChild(document.createTextNode(serviceToString(service)));
    li.id = service.name;
    !animated || li.classList.add('slide-right');
    return li;
}

function addService(service, animated) {
    const li = document.createElement("li");
    listContainer.appendChild(createServiceElement(li, service, animated));
}

function modifyService(service) {
    const li = listContainer.querySelector('#' + service.name);
    li.innerHTML = '';
    createServiceElement(li, service, true);
}

setSnackBar = (text) => {
    let snackBar = document.createElement('li');
    snackBar.appendChild(document.createTextNode(text));
    snackBars.appendChild(snackBar);
    setTimeout(() => snackBars.removeChild(snackBar), 5000);
}

function resetForm() {
    urlName.value = '';
    serviceName.value = '';
}

function getService() {
    fetch(servicesRequest)
        .then(function (response) {
            return response.json();
        })
        .then(function (serviceList) {
            listContainer.innerHTML = "";
            serviceList.forEach(service => {
                addService(service, false);
            });
            setSnackBar('Service fetched')
        });
}

getService();

setInterval(() => getService(), 60000);

saveButton.onclick = () => {
    fetch('http://localhost:8080/service', {
            method: 'post',
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({url: urlName.value, name: serviceName.value})
        }
    ).then(response => {
        if (!response.ok) {
            return
        }
        if (response.status === 201) {
            setSnackBar('Service : ' + serviceName.value + ' | URL :' + urlName.value + ' posted');
            addService({name: serviceName.value, url: urlName.value, alive: 'PENDING'}, true);
        }
        if (response.status === 200) {
            setSnackBar('Service : ' + serviceName.value + ' updated');
            modifyService({name: serviceName.value, url: urlName.value, alive: 'PENDING'})
        }
        resetForm();
    });
}
deleteButton.onclick = () => {
    let url = new URL('http://localhost:8080/service');
    url.search = new URLSearchParams({name: serviceName.value}).toString();
    fetch(url, {
            method: 'delete',
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'Content-Type': 'application/json'
            }
        }
    ).then(response => {
        if (response.status === 202) {
            setSnackBar('Service : ' + serviceName.value + ' deleted');
            let liElement = listContainer.querySelector('#' + serviceName.value);
            setTimeout(() => listContainer.removeChild(liElement), 450);
            liElement.classList.add('slide-left');
            resetForm();
        }
    });
}
