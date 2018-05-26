angular.module('MyApp')
  .controller('ContactCtrl', function($scope, $auth, $rootScope, $ngConfirm, $timeout, toastr, Contact) {
        $scope.locationId = null;
        
        if ($scope.initialized == null) {
        	$rootScope.editingState = 0;
        	$scope.initialized = true;
        }
                
        $rootScope.$on('ShowContacts', function(event, args) {
        	$(window).trigger('resize');
        	$scope.locationId = args.id;
        	$scope.getAll();
        });
        
        $rootScope.$on('HideContacts', function(event, args) {
        	$scope.locationId = null;
        	$rootScope.editingState = 0;
        });
        
        $scope.formatTable = function () {

        	if ($rootScope.editingState == 1) {
            	var locationFormBottom = $('#locationForm').offset().top + $('#locationForm').outerHeight(true);
        		var contactListHeight = $(window).height() - locationFormBottom - 30;

        		if (contactListHeight < 200)
        			contactListHeight = 200;
        		
        		$('#contactList').height(contactListHeight);

				$("#contactListTableBody tr:even").addClass("evenClass");
        		$("#contactListTable").freezeHeader({ 'height': contactListHeight-40 + "px" });
			}
        }
        
        $( window ).resize(function() {
        	$timeout($scope.formatTable, 1);
        });
        
        $scope.reportError = function(response) {

        	if (response.status === 0) {
    	    	toastr.error("Unable to contact server, possible network issue");
    	    	throw "exit";

        	} else if (response.status === 422) {

        		for (var error in response.data.errors) {
    		    	toastr.error(response.data.errors[error]);
    		    }

        	} else if (response.message != undefined) {

        		if (response.message.data != undefined)
        			toastr.error(response.message.data, response.status);
        		else
            		toastr.error(response.message, response.status);

        	} else if (response.data != undefined) {
        		
        		if (response.data.message != undefined)
        			toastr.error(response.data.message, response.status);

        		else if (response.data.errors != undefined)
        			toastr.error(response.data.errors, response.status);
        		
        		else
        			toastr.error(response.data, response.status);

        	} else
        		toastr.error(response, response.status);
        }
        
        $scope.getAll = function() {
        	Contact.getAll($scope.locationId)
                .then(function (result) {
                	$rootScope.contacts = result.data;
            		$rootScope.editingState = 1;
                	$timeout($scope.formatTable, 1);
                })
                .catch(function(response) {
                	$scope.reportError(response);
                	$rootScope.editingState = 1;
                });
        }

        $scope.save = function (contact) {
        	
        	contact.locationId = $scope.locationId;
            Contact.save(contact)
                .then(function (result) {
                	$scope.initCreateContactForm();
                    $scope.getAll();
					toastr.success('Contact has been saved');
                })
                .catch(function(response) {
                	$scope.reportError(response);
                  });
        }

        $scope.update = function (contact) {

        	if ($scope.contactForm.$dirty == false) {
            	$scope.reportError("No changes to save");    	
            	return false;
        	}
        	
        	$scope.save(contact);
        	$scope.contactForm.$setPristine();
        }

        $scope.delete = function (contactId) {

        	$ngConfirm({
        		title: 'Are you sure?',
        		content: 'This action is not reversable',
        		containerFluid: true,
        		buttons: {
        	        yes: {
        	            text: 'Yes', 
        	            action: function(){
        	            	Contact.delete(contactId)
        	                .then(function (result) {
    			            	$scope.contactForm.$setPristine();
        	                	$scope.cancelEditing();
        						toastr.success('Contact has been deleted');
        	                })
        	                .catch(function(response) {
        	                	$scope.reportError(response);
        	                });
        	            }
        	        },
        	        no: {
        	            text: 'No'
        	        }
        	    }
        	})
        }

        $scope.initCreateContactForm = function () {
            $scope.newContact = { id: '', locationId: '', contactType: '', contactName: '', phoneNumber: '', emailAddress: '' };
        }

        $scope.setCreateContact = function () {
        	$rootScope.editingState = 2;
    	    $('#createContactHolder').css({top: '50%'});
    	    $('#createContactHolder').animate({top: '20%'}, 1000);
        }
        
        $scope.setEditedContact = function (contact) {
        	$rootScope.editedContact = angular.copy(contact);
            $rootScope.editingState = 3;
    	    $('#editContactHolder').css({top: '50%'});
    	    $('#editContactHolder').animate({top: '20%'}, 1000);
        }

        $scope.isDirty = function(completeFunction) {

        	if ($scope.contactForm.$dirty == true) {

        		$ngConfirm({
    	    		title: 'Form has unsaved changes',
    	    		content: 'Are you sure you want to continue?',
    	    		containerFluid: true,
    	    		scope: $scope,
    	    		buttons: {
    	    	        yes: {
    	    	            text: 'Yes', 
    	    	            action: function(){
    	    	            	eval(completeFunction);
    	    	            }
    	    	        },
    	    	        no: {
    	    	            text: 'No'
    	    	        }
    	    	    }
    	    	});
        	} else {
            	eval(completeFunction);
        	}
        }
        
        $scope.cancelEditingComplete = function () {
            $scope.getAll();
        }

        $scope.cancelEditing = function () {
    		$scope.isDirty("$scope.cancelEditingComplete()");
        }
        
    	$scope.initCreateContactForm();
    });