angular.module('MyApp')
  .controller('SignupCtrl', function($scope, $location, $auth, toastr, vcRecaptchaService) {

	  $scope.response = null;
      $scope.widgetId = null;
      
      $scope.model = {
          key: '6Le-_isUAAAAAPfsYgEMwL1sFfWL0SNBxlhgxlXU'
      };
      
      $scope.setResponse = function (response) {
          console.info('Response available');
          $scope.response = response;
      };
      
      $scope.setWidgetId = function (widgetId) {
          console.info('Created widget ID: %s', widgetId);
          $scope.widgetId = widgetId;
      };

      $scope.cbExpiration = function() {
          console.info('Captcha expired. Resetting response object');
          vcRecaptchaService.reload($scope.widgetId);
          $scope.response = null;
       };

	  $scope.signup = function() {

	      var captchaResponse = $scope.response;
	      $scope.user.captchaResponse = $scope.response;

		  $auth.signup($scope.user)
	        .then(function(response) {
	          $auth.setToken(response);
	          $location.path('/');
	          toastr.info('You have successfully created a new account and have been signed-in');
	        })
	        .catch(function(response) {
	          vcRecaptchaService.reload($scope.widgetId);
	          toastr.error(response.data.message);
	        });
    };
  });