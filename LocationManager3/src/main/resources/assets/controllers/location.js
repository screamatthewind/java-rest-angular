angular.module('MyApp')
  .controller('LocationCtrl', function($scope, $auth, $state, $rootScope, $ngConfirm, $timeout, toastr, Location, Account) {

	// margin-auto in bootstrap.css kept making screen jump on occasion
	$scope.centerContainer = function() {
	    pageWidth = $(window).width();
	    myElementWidth = $('.container').width();
	
		$('.container').css('margin-left', (pageWidth / 2) - (myElementWidth / 2) + "px");
	}

	$(window).resize(function() {
		$scope.centerContainer();
	});

	if ($scope.initialized == null) {
    	$scope.initialized = true;
    	$scope.centerContainer();
    }
    
	$scope.navTitle = 'All Locations';
    $scope.operation="";
    $scope.isSaveVisible = false;
    $scope.isDeleteVisible = false;

    $scope.formatTable = function () {
    	$("#locationsTable tr:even").addClass("evenClass");
    }

    $scope.reportError = function(response) {

    	if (response.status === 422) {
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

	$scope.getLocations = function() {
      Location.getLocations()
        .then(function(response) {
          $scope.locations = response.data;
          $timeout($scope.formatTable, 1);
        })
        .catch(function(response) {
        	$scope.reportError(response);
        });
    };
    
    $scope.getLocation = function(id) {
        Location.getLocation(id)
          .then(function(response) {
            $scope.location = response.data;
			$scope.operation="update";
			$scope.isSaveVisible = true;
			$scope.isDeleteVisible = true;
			$scope.unlockInput();
			$rootScope.$broadcast('ShowContacts', {id: id});
          })
          .catch(function(response) {
          	$scope.reportError(response);
          });
    };
    
    $scope.saveLocation = function() {
    	Account.getProfile()
	        .then(function(response) {

	        	if ($scope.operation == "create") {
		        	Location.saveLocation($scope.location)
		        		.then(function(response) {
							toastr.success('Location has been created');
							$scope.isDeleteVisible = true;
							$scope.operation = "update";
	
				        	$scope.location = response.data;
			            	$scope.locationForm.$setPristine();
							$scope.getLocations();

							$rootScope.$broadcast('ShowContacts', {id: response.data.id});
			        	})
				        .catch(function(response) {
				        	$scope.reportError(response);
				        });

	        	} else {
		        	Location.updateLocation($scope.location)
	        		.then(function(response) {
						toastr.success('Location has been updated');
						$scope.isDeleteVisible = true;

			        	$scope.location = response.data;
		            	$scope.locationForm.$setPristine();
						$scope.getLocations();

						$rootScope.$broadcast('ShowContacts', {id: response.data.id});
	        		})
			        .catch(function(response) {
			        	$scope.reportError(response);
			        });
		        }
	        })
	        .catch(function(response) {
	        	toastr.error('Cannot get user: ' + $scope.reportError(response));
	        });
    	
    };

    $scope.deleteLocation = function(id) {
    	$ngConfirm({
    		title: 'Are you sure?',
    		content: 'This action is not reversable',
    		containerFluid: true,
    		buttons: {
    	        yes: {
    	            text: 'Yes', 
    	            action: function(){
    	               Location.deleteLocation(id)
    	                .then(function(response) {
    		        		toastr.success('Location has been deleted');
    	      				$scope.resetForm(false);
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
    };

    // get rid of chrome auto-complete yellow color
    $scope.lockInput = function() {
    	$(":text").each(function() {
    		$(this).removeClass("enabledInput");
    		$(this).addClass("disabledInput");
    	});
    	
    	$scope.isFieldLocked = true;
    };

    // get rid of chrome auto-complete yellow color
    $scope.unlockInput = function() {
    	$(":text").each(function() {
    		$(this).removeClass("disabledInput");
    		$(this).addClass("enabledInput");
    	});
    	
    	$scope.isFieldLocked = false;
    };

    $scope.clearForm = function() {
    	$scope.location = {
    		id:'',
    		locationName:'',
    		address1:'',
    		address2:'',
    		city:'',
    		state:'',
    		zip:'',
    		countryCode:''
    	};

		$rootScope.$broadcast('HideContacts', {id: null});
    };

    $scope.isDirty = function(completeFunction) {

    	if ($scope.locationForm.$dirty == true) {

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

    $scope.addNewComplete = function(element) {

    	$scope.clearForm();
    	$scope.isSaveVisible = true;
    	$scope.isDeleteVisible = false;
    	$scope.unlockInput();
    	$scope.getLocations();
    	$scope.operation="create";
    	$scope.locationForm.$setPristine();
    }
    
    $scope.addNew = function(element) {
    	$scope.isDirty("$scope.addNewComplete()");
    };

    $scope.resetFormComplete = function(name) {
		$scope.operation=null;
		$scope.clearForm();
		
		$scope.isSaveVisible = false;
		$scope.isDeleteVisible = false;
		$scope.lockInput();
	
		$scope.getLocations();
		
    	$scope.locationForm.$setPristine();
    }

    $scope.resetForm = function(validate) {
    	if (validate)
    		$scope.isDirty("$scope.resetFormComplete()");
    	else
    		$scope.resetFormComplete();

    };

    $scope.isValid = function(value) {
        return !value
    }

    $scope.lockInput();
    $scope.getLocations();
});
