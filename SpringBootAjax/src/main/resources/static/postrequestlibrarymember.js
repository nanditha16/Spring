$(document).ready(		
		function() {

			// SUBMIT FORM
			$("#librayMemberRegistrationForm").submit(function(event) {
				// Prevent the form from submitting via the browser.
				event.preventDefault();
				ajaxPost();
			});

			function ajaxPost() {

				let addressVal = {
						city: $("#city").val(),
						state: $("#state").val(),
						pincode: $("#pincode").val()
				}
				
				// PREPARE FORM DATA
				let formData = {
					memberId : $("#memberId").val(),
					memberName : $("#memberName").val(),
					gender : $("#gender").val(),
					email : $("email").val(),
					address: addressVal
				}

				// DO POST
				$.ajax({
					type : "POST",
					contentType : "application/json",
					url : "saveLibraryMember",
					data : JSON.stringify(formData),
					dataType : 'json',
					success : function(result) {
						if (result.status == "success") {
							$("#postResultDiv").html(
									"" + result.data.bookName
											+ "Post Successfully! <br>"
											+ "---> Congrats !!" + "</p>");
						} else {
							$("#postResultDiv").html("<strong>Error</strong>");
						}
						console.log(result);
					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});

			}

		})