const addBtn = document.getElementById('add-btn')
const productName = document.getElementById('productName')
const description = document.getElementById('description')
const quantity = document.getElementById('quantity')
const price = document.getElementById('price')
const category = document.getElementById('category')
const image = document.getElementById('image')
const productNameError = document.getElementById('product-name-error')
const descriptionError = document.getElementById('description-error')
const quantityError = document.getElementById('quantity-error')
const priceError = document.getElementById('price-error')
const categoryError = document.getElementById('category-error')
const imageError = document.getElementById('image-error')

addBtn.addEventListener('click', (e) => {
    let isValid

    if (productName.value.trim() === '') {
        productNameError.innerHTML = `Product's name is required`
        productName.value = ''
    } else {
        productNameError.innerHTML = ''
    }

    if (description.value.trim() === '') {
        descriptionError.innerHTML = 'Description is required'
        description.value = ''
    } else {
        descriptionError.innerHTML = ''
    }

    if (category.value === '') {
        categoryError.innerHTML = 'Category is required'
    } else {
        categoryError.innerHTML = ''
    }

    if (image.value === '') {
        imageError.innerHTML = 'Image is required'
    } else {
        imageError.innerHTML = ''
    }

    if (!RegExp('^[0-9]+([.][0]+)?$').test(quantity.value.trim())) {
        quantityError.innerHTML = 'A unsigned integer is required'
        quantity.value = quantity.value.trim()
    } else {
        quantityError.innerHTML = ''
    }

    if (!RegExp('^([0-9]+[.])?[0-9]+$').test(price.value.trim())) {
        priceError.innerHTML = 'A unsigned real number is required'
        price.value = price.value.trim()
    } else {
        priceError.innerHTML = ''
    }

    if (productNameError.innerHTML !== '' || descriptionError.innerHTML !== '' || categoryError.innerHTML !== '' || imageError.innerHTML !== '' || quantityError.innerHTML !== '' || priceError.innerHTML !== '') {
        isValid = false;
    } else {
        isValid = true;
    }

    if (!isValid) {
        e.preventDefault()
    }
})