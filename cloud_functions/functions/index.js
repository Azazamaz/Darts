const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref("Notifications")
.onWrite((change, context) => {
	var request = change.after.val();
	var payload = {
		data: {
			title: "New online game",
			body: "A new online darts game started"
		}
	};
	
	return admin.messaging().sendToDevice(request.token, payload)
	.then(function(response) {
		console.log("Message sent: ", response);
	})
	.catch(function(error) {
		console.log("Error while sending message: ", error);
	})
});