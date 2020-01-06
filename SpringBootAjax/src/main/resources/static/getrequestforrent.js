GET: $(document).ready(
		function() {
			
			// GET REQUEST
			$("#rentableBooks").click(function(event) {
				this.disabled = true;
				event.preventDefault();
				ajaxGet();
			});

			// DO GET
			function ajaxGet() {
				$.ajax({
					type : "GET",
					url : "getRentableBooks",
					success : function(result) {
						if (result.status == "success") {
									var $deviceList = $('#device-list');
									
									var $modal = $('#quick-view-device-modal');
									var modalShowHndl = function (evt) {
										var button = evt.data.relatedTarget;
									}
									var $relatedTarget;
									var bookIdGlobal;
						
									$.each(result.data,function(i, book) {
										var deviceName = "Book Name = "
											+ book.bookName
											+ ", Author  = " + book.bookAuthor;
										
										// The result is set as button
									  var button = $('<button/>')
									  .text(' ' + deviceName)
									  .addClass('list-group-item device')
									  .attr({name:deviceName,
									         "aria-label": "Quick View Device",
									         "data-toggle": "modal",
									         type: "button"});

									  button.prepend(' ' + book.bookId + ' - ');
									  $deviceList.append(button);
								})
							
								
									
									$deviceList.on('click', 'button', function(evt) {
										$(this).removeData();
									  if($(evt.target).hasClass('glyphicon-minus-sign')) {
										 console.log('delete device:',evt.target.parentNode.textContent)
									  } else {
										 $relatedTarget = $(evt.target);
									    $modal.one('show.bs.modal', {relatedTarget: $relatedTarget}, modalShowHndl)
									    $modal.modal('show')
									    bookIdGlobal =  $relatedTarget.val();	
									    
									  }
									})
									
									
									$('#rentBook').click(function(e) {
										e.preventDefault()
										
										alert("Change the bookStatus to Rented and update the library member!!"  );
										
										let formData = {
												memberIdRent : $("#memberIdRent").val(),
												bookIdRent : $("#bookIdRent").val(),
										}
  
										$.ajax({
										    type: 'POST', // Use POST with X-HTTP-Method-Override or a straight PUT if appropriate.
										    dataType: 'json', // Set datatype - affects Accept header
										    url: "rentBookByBookId/" + $("#memberIdRent").val() + "/"+ $("#bookIdRent").val(), // A valid URL
										    data: JSON.stringify(formData) // Some data e.g. Valid JSON as a string
										});

									});
								
							console.log("Success: ", result);
							} else {
								
							$("#getResultDiv").html("<strong>Error</strong>");
							console.log("Fail: ", result);
						}
					},
					error : function(e) {
						$("#getResultDiv").html("<strong>Error</strong>");
						console.log("ERROR: ", e);
					}
				});
			}
		})