/* global parseFloat */

// Handle quantity increasement and descreasement
const paymentTotal = document.getElementById('payment-total')
const quantityValues = document.getElementsByClassName('quantity-value')
const increaseAmountBtns = document.getElementsByClassName('increase-amount-btn')
const descreaseAmountBtns = document.getElementsByClassName('descrease-amount-btn')
const priceValues = document.getElementsByClassName('price-value')
const productPriceTotals = document.getElementsByClassName('product-price-total')
const length = productPriceTotals.length

paymentTotal.value = parseFloat(paymentTotal.value).toFixed(2)

for (let i = 0; i < length; i++) {
    priceValues[i].value = parseFloat(priceValues[i].value).toFixed(2)
    productPriceTotals[i].value = parseFloat(productPriceTotals[i].value).toFixed(2) 

    increaseAmountBtns[i].addEventListener('click', () => {
        quantityValues[i].value = parseInt(quantityValues[i].value) + 1
        let value = parseInt(quantityValues[i].value) * parseFloat(priceValues[i].value)
        productPriceTotals[i].value = value.toFixed(2)
        let total = parseFloat(paymentTotal.value) + parseFloat(priceValues[i].value)
        paymentTotal.value = total.toFixed(2)
    })

    descreaseAmountBtns[i].addEventListener('click', () => {
        if (parseInt(quantityValues[i].value) > 1) {
            quantityValues[i].value = parseInt(quantityValues[i].value) - 1
            let value = parseInt(quantityValues[i].value) * parseFloat(priceValues[i].value)
            productPriceTotals[i].value = value.toFixed(2)
            let total = parseFloat(paymentTotal.value) - parseFloat(priceValues[i].value)
            paymentTotal.value = total.toFixed(2)
        }
    })

    quantityValues[i].addEventListener('change', () => {
        const quantity = quantityValues[i].value
        if (!RegExp('^[0-9]+([.][0]+)?$').test(quantity) || quantity == 0) {
            quantityValues[i].value = 1
        } else {
            quantityValues[i].value = parseInt(quantity)
        }

        let value = parseInt(quantityValues[i].value) * parseFloat(priceValues[i].value)
        productPriceTotals[i].value = value

        paymentTotal.value = 0
        for (let i = 0; i < length; i++) {
            let total = parseFloat(paymentTotal.value) + parseInt(quantityValues[i].value) * parseFloat(priceValues[i].value)
            paymentTotal.value = total.toFixed(2)
        }
    })
}

// Handle deleting products
const productDeletingBtns = document.getElementsByClassName('product-deleting-btn')

for (let i = 0; i < length; i++) {
    productDeletingBtns[i].addEventListener('click', (e) => {
        const doesDeletes = confirm('Are you sure you want to delete this product from cart')
        if (!doesDeletes) e.preventDefault()
    })
}

// Handle ordering 
const orderBtn = document.getElementById('order-btn')

orderBtn.addEventListener('click', (e) => {
    const doesOrders = confirm('Are you sure you want to order')
    if (!doesOrders) e.preventDefault()
})
