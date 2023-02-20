async function sendSignInData(data) {
    return await fetch('http://localhost:8080/api/v1/login', {
        method: 'POST',
        body: data,
    })
}

async function sendLogInData(data) {
    return await fetch('http://localhost:8080/api/v1/users', {
        method: 'POST',
        body: data,
    })
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

async function handleSignInForm(event) {
    event.preventDefault()
    const data = new FormData(event.target)
    const response = await sendSignInData(data)
    if (response.status === 200) {
        const body = await response.json()
        alert("Успешная авторизация:\nТокен:" + body["token"])
    }
    else alert(response.status)
}

async function handleLogInForm(event) {
    event.preventDefault()
    const data = new FormData(event.target)
    const response = await sendLogInData(data)
    if (response.status === 200) {
        const body = await response.json()
        alert("Успешная регистрация:\nТокен:" + body["token"])
    }
    else alert(response.status)
}

const signInForm = document.getElementById('sign_in')
signInForm.addEventListener('submit', handleSignInForm)

const logInForm = document.getElementById('register')
logInForm.addEventListener('submit', handleLogInForm)