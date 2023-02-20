import React from "react";
import '../../App.css';

async function sendSignInData(data) {
    return await fetch('http://localhost:8080/api/v1/login', {
        method: 'POST',
        body: data,
    })
}

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

const signInForm = document.getElementById('sign_in')
signInForm.addEventListener('submit', handleSignInForm)

class SigninPage extends React.Component {
    render() {
        return (
            <div>
                <a href="/home" className="description_font">
                    Назад
                </a>

                <h1 className='header_font' style={{textAlign: 'center'}}>
                    Вход в аккаунт
                </h1>

                <form action="http://localhost:8080/api/v1/login" method="post" id="sign_in">
                    <center>
                        <input type="email" id="email" name="mail" placeholder="Почта" className="text_input"/>
                    </center>
                    <center>
                        <input type="password" id="pass" name="password" placeholder="Пароль" className="text_input"/>
                    </center>
                    <center>
                        <button type="submit" className="button">
                            Войти
                        </button>
                    </center>
                </form>
                <center>
                    <a href="/login" className="label_font">
                        Зарегистрироваться
                    </a>
                </center>
            </div>
        )
    }
}

export default SigninPage;