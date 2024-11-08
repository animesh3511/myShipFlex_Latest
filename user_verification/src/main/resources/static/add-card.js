const stripe = Stripe('pk_test_51Py4ib082jp7x4ErO7NLNwx9lunuu3GXt8OXy8yC6TsNJpDKBlvKjKEknuXpJ6nRrZ9QB0IKIssfTJCdsQTyPG5N00ZpYrTwz0');

const cardElement = document.getElementById('card-element');
const emailInput = document.getElementById('email'); // Get the email input element

const card = stripe.elements().create('card', {
    style: {
        base: {
            fontSize: '16px',
            color: '#32325d',
        },
    },
});

card.mount(cardElement);



document.getElementById('add-card-button').addEventListener('click', async (e) => {
    e.preventDefault();


    const email = emailInput.value;

    try {

        const response = await fetch('/createPaymentIntent', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                email: email,
            }),
        });

        if (!response.ok) {
            const err = await response.json();
            throw new Error(err.error);
        }

        const data = await response.json();
        console.log('Data received:', data);
        const clientSecret = data.client_secret;


        if (!clientSecret) {
            throw new Error('Missing client secret from server response');
        }


        const { paymentMethod } = await stripe.createPaymentMethod({
            type: 'card',
            card: card,
        });


        const attachResponse = await fetch('/attach-payment-method', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                email: email, // Send the email instead of customer ID
                payment_method_id: paymentMethod.id,
            }),
        });

        if (!attachResponse.ok) {
            const errorData = await attachResponse.json();
            throw new Error(errorData.error || 'Failed to attach payment method');
        }

        const attachData = await attachResponse.json();
        console.log('Payment method attached successfully:', attachData);

        const { error } = await stripe.confirmCardPayment(clientSecret, {
            payment_method: paymentMethod.id,
        });

        if (error) {
            console.error('Error confirming payment:', error);
        } else {
            console.log('Payment method attached successfully!');
        }
    } catch (error) {
        console.error('Error fetching client secret:', error);
    }
});
