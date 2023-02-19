async function sendData(data) {
    const xhr = new XMLHttpRequest();
    xhr.open('POST','http://localhost:8080/api/v1/login');
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify(Object.fromEntries(data)));
    xhr.onload = function() {
        if (xhr.status === 200) {
            return xhr.response.message;
        }
        else return xhr.status;
    }
    xhr.onerror = function(e) {
        alert(xhr.status);
    };
}

/*function onSuccess(body, formData) {
    for (const item in formData) {
        if (item[0] === "type" && item[1] === "musician") {
            window.location.href = 'musician_main.html';
        }
        else if (item[0] === "type" && item[1] === "owner") {
            window.location.href = 'owner_main.html';
        }
    }
}*/

async function handleFormSubmit(event) {
    event.preventDefault()
    const data = new FormData(event.target)
    const token = await sendData(data)
    alert(token)
}

const signInForm = document.getElementById('sign_in')
signInForm.addEventListener('submit', handleFormSubmit)