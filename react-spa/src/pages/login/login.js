import React from "react";
import '../../App.css';

//const logInForm = document.getElementById('register')
//logInForm.addEventListener('submit', handleLogInForm)

class LoginPage extends React.Component {
    async sendLogInData(data) {
        return await fetch('http://localhost:8080/api/v1/users', {
            method: 'POST',
            body: data,
        })
    }

    async handleLogInForm(event) {
        event.preventDefault()
        const data = new FormData(event.target)
        const response = await this.sendLogInData(data)
        if (response.status === 200) {
            const body = await response.json()
            alert("Успешная регистрация:\nТокен:" + body["token"])
        }
        else alert(response.status)
    }

    render() {
        return (
            <div>
                <a href="/home" className="description_font" style={{marginLeft: 15}}>
                    Назад
                </a>

                <h1 className='header_font' style={{textAlign: 'center'}}>
                    Регистрация
                </h1>

                <form action="http://localhost:8080/api/v1/users" method="post" id="register">
                    <center>
                        <input type="text" id="name" name="fio" placeholder="ФИО" className="text_input"/>
                    </center>
                    <center>
                        <input type="email" id="email" name="mail" placeholder="Почта" className="text_input"/>
                    </center>
                    <center>
                        <input type="tel" id="tel" name="phone" placeholder="Телефон" className="text_input"/>
                    </center>
                    <center>
                        <input type="password" id="pass" name="password" placeholder="Пароль" className="text_input"/>
                    </center>
                    <center>
                        <input type="radio" id="musician" value="musician" name="type" checked/>
                        <label for="musician" className="label_font">Я музыкант</label>
                        <input type="radio" id="owner" value="owner" name="type"/>
                        <label for="owner" className="label_font">Я владелец</label>
                    </center>
                    <center>
                        <button type="submit" className="button" onSubmit={this.handleLogInForm}>
                            Зарегистрироваться
                        </button>
                    </center>
                </form>
                <center>
                    <a href="/signin" className="label_font">
                        У меня уже есть аккаунт
                    </a>
                </center>
            </div>
        )
    }
}

export default LoginPage;