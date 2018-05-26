angular.module('MyApp')
  .controller('LoginCtrl', function($scope, $location, $auth, toastr, vcRecaptchaService) {

	  $scope.response = null;
      $scope.widgetId = null;
      
      $scope.model = {
          key: '6Le-_isUAAAAAPfsYgEMwL1sFfWL0SNBxlhgxlXU'
      };
      
      $scope.setResponse = function (response) {
          // console.info('Response available');
          $scope.response = response;
      };
      
      $scope.setWidgetId = function (widgetId) {
          // console.info('Created widget ID: %s', widgetId);
          $scope.widgetId = widgetId;
      };

      $scope.cbExpiration = function() {
          // console.info('Captcha expired. Resetting response object');
          vcRecaptchaService.reload($scope.widgetId);
          $scope.response = null;
       };
	  
	  $scope.login = function() {

	    var captchaResponse = $scope.response;
	    $scope.user.captchaResponse = $scope.response;

	    $auth.login($scope.user)
        .then(function(response) {
          toastr.success('You have successfully signed in!');
//          $scope.invokeCSCode(response.data.token);
          $location.path('/');
        })
        .catch(function(error) {
          toastr.error(error.data.message, error.status);
          vcRecaptchaService.reload($scope.widgetId);
        });
    };
    
	  $scope.loginX = function() {

		    var captchaResponse = $scope.response;
		    $scope.user.captchaResponse = $scope.response;

		    $auth.login($scope.user)
	        .then(function(response) {
	          toastr.success('You have successfully signed in!');
	          $scope.invokeCSCode(response.data.token);
	          // $location.path('/');
	        })
	        .catch(function(error) {
	          toastr.error(error.data.message, error.status);
	          vcRecaptchaService.reload($scope.widgetId);
	        });
	    };

        $scope.invokeCSCode = function (data) {
    	try {
    	    alert("Sending Data:" + data);
    	    invokeCSharpAction(data);
    	}
    	catch (err){
    		alert(err);
    	}
    }
    
    $scope.authenticate = function(provider) {
      $auth.authenticate(provider)
        .then(function() {
          toastr.success('You have successfully signed in with ' + provider + '!');
          $location.path('/');
        })
        .catch(function(error) {
        	vcRecaptchaService.reload($scope.widgetId);
        	if (error.message) {
            // Satellizer promise reject error.
            toastr.error(error.message);
          } else if (error.data) {
            // HTTP response error from server
            toastr.error(error.data.message, error.status);
          } else {
            toastr.error(error);
          }
        });
    };
  });
