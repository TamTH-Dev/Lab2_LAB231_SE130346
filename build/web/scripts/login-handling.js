/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
const loginBtn = document.getElementById('login-btn')
const email = document.getElementById('email')
const password = document.getElementById('password')
const emailError = document.getElementById('email-error')
const passwordError = document.getElementById('password-error')

loginBtn.addEventListener('click', (e) => {
    let isValid
    const emailRegx = /^[a-z][a-z0-9_\.]{5,32}@[a-z0-9]{2,}(\.[a-z0-9]{2,4}){1,2}$/ig
    const passwordRegx = /^[a-z][a-z0-9_@\.]{7,}$/ig

    if (!emailRegx.test(email.value)) {
        emailError.innerHTML = 'Email is invalid!'
    } else {
        emailError.innerHTML = ''
    }

    if (!passwordRegx.test(password.value)) {
        passwordError.innerHTML = 'Password is invalid!'
        password.innerHTML = ''
    } else {
        passwordError.innerHTML = ''
    }

    if (emailError.innerHTML == '' && passwordError.innerHTML == '') {
        isValid = true
    } else {
        isValid = false
    }

    if (!isValid) {
        e.preventDefault()
    }
})


