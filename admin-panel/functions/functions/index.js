const functions = require('firebase-functions')
const admin = require('firebase-admin')

admin.initializeApp(functions.config().firebase);

const stripe = require('stripe')(functions.config().stripe.token)

exports.stripeCharge = functions.database
                                .ref('/Users/{userId}/')
                                .onWrite(event => {

const payment = event.data.val();

if(!payment || payment,charge) return;

return admin.database()
            .ref(`Users/${userId}`)
            .once('value')
            .then(snapshot => {
                return snapshot.val();
            })
            .then(customer => {
                const amount = payment.balance;
                const idempotency_key = paymentId;
                const source = payment.token.id;
                const currency = 'usd';
                const charge = {amount, currency, source};

                return stripe.charges.create(charge, {idempotency_key});
            })

            .then(charge => {
                admin.database()
                .ref(`/Users/${userId}/balance`)
                .set(charge)
            })


     })

