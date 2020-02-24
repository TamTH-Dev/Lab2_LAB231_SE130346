const increaseAmountBtn = document.getElementById('increase-amount-btn')
const descreaseAmountBtn = document.getElementById('descrease-amount-btn')
const productAmount = document.getElementById('product-amount')

increaseAmountBtn.addEventListener('click', () => {
    productAmount.value = parseInt(productAmount.value) + 1
})

descreaseAmountBtn.addEventListener('click', () => {
    if (parseInt(productAmount.value) > 1) {
        productAmount.value = parseInt(productAmount.value) - 1
    }
})

productAmount.addEventListener('change', () => {
    const productAmountValue = productAmount.value
    if (!RegExp('^[0-9]+([.][0]+)?$').test(productAmountValue) || productAmountValue == 0) {
        productAmount.value = 1
    } else {
        productAmount.value = parseInt(productAmountValue)
    }
})