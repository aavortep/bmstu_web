/* eslint-disable react/jsx-no-comment-textnodes */
import React from 'react';
import '../../App.css';

class HomePage extends React.Component {
    render() {
        return (
            <div>
                <a href='login' className='label_font' style={{marginRight: 15, float: 'right'}}>
                    Зарегистрироваться
                </a>
                <a href='signin' className='label_font' style={{marginRight: 15, float: 'right'}}>
                    Войти
                </a>

                <h1 className='header_font' style={{marginLeft: 90}}>
                    Добро пожаловать в HearBase!
                </h1>

                <div id = "base1"></div>
                <div id = "base2"></div>
                <div id = "base3"></div>

                <p className='description_font' style={{marginLeft: 760, lineHeight: 1.5, marginTop: 42}}>
                    HearBase - платформа для бронирования репетиционных баз.
                </p>

                <div className='highlight' style={{marginLeft: 760, minHeight: 47, textAlign: 'center', padding: 2,
                marginRight: 335, marginTop: 30}}>
                    <p>Музыкант?</p>
                </div>
                <p className='label_font' style={{marginLeft: 760, lineHeight: 1.5, marginTop: 15, marginRight: 20}}>
                    Здесь ты сможешь найти и забронировать подходящую для себя репетиционную базу.
                </p>

                <div className='highlight' style={{marginLeft: 760, minHeight: 47, textAlign: 'center', padding: 2,
                marginRight: 335, marginTop: 30}}>
                    <p>Владелец?</p>
                </div>
                <p className='label_font' style={{marginLeft: 760, lineHeight: 1.5, marginTop: 15, marginRight: 20}}>
                    Здесь ты сможешь отслеживать и контролировать записи на репетиции в свои репетиционные базы.
                </p>
            </div>
        );
    }
}

export default HomePage;