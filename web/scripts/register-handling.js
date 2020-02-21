/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
const registerBtn = document.getElementById('register-btn')
const email = document.getElementById('email')
const username = document.getElementById('username')
const password = document.getElementById('password')
const passwordConfirm = document.getElementById('password-confirm')
const emailError = document.getElementById('email-error')
const usernameError = document.getElementById('username-error')
const passwordError = document.getElementById('password-error')
const passwordConfirmError = document.getElementById('password-confirm-error')

registerBtn.addEventListener('click', (e) => {
    let isValid
    const emailRegx = /^[a-z][a-z0-9_\.]{5,32}@[a-z0-9]{2,}(\.[a-z0-9]{2,4}){1,2}$/ig
    const passwordRegx = /^[a-z][a-z0-9_@\.]{7,}$/ig
    const usernameRegx = /^[a-z][a-z0-9]{0,}$/ig

    if (!emailRegx.test(email.value)) {
        emailError.innerHTML = 'Email is invalid!'
    } else {
        emailError.innerHTML = ''
    }

    if (!usernameRegx.test(username.value)) {
        usernameError.innerHTML = 'Username is invalid!'
    } else {
        usernameError.innerHTML = ''
    }

    if (!passwordRegx.test(password.value)) {
        passwordError.innerHTML = 'Password is invalid!'
        password.innerHTML = ''
    } else {
        passwordError.innerHTML = ''
    }

    if (emailError.innerHTML == '' && usernameError.innerHTML == '' && passwordError.innerHTML == '') {
        if (passwordConfirm.value != password.value) {
            passwordConfirmError.innerHTML = 'Password Confirm must be matched with Password'
            passwordConfirm.innerHTML = ''
        } else {
            passwordConfirmError.innerHTML = ''
        }
    }
    if (emailError.innerHTML == '' && usernameError.innerHTML == '' && passwordError.innerHTML == '' && passwordConfirmError.innerHTML == '') {
        isValid = true
    } else {
        isValid = false
    }

    if (!isValid) {
        e.preventDefault()
    }
})



