angular.module('MyApp', ['ngResource', 'ngMessages', 'ngAnimate', 'toastr', 'ui.router', 'satellizer', 'blockUI', 'cp.ngConfirm', 'vcRecaptcha'])
  .config(function($stateProvider, $urlRouterProvider, $authProvider, blockUIConfig) {

	// blockUIConfig.message = 'Please wait';
	
	blockUIConfig.requestFilter = function(config) {

		  var message;

		  switch(config.method) {
		    case 'GET':
		      message = 'Loading ...';
		      break;

		    case 'POST':
		      if (config.url == '/auth/login')
			      message = 'Logging in ...';
		      else
		    	  message = 'Saving ...';
		      break;

		    case 'DELETE':
		      message = 'Deleting ...';
		      break;

		    case 'PUT':
		      message = 'Updating ...';
		      break;
		  };

		  return message;
		};
	
	
    /**
     * Helper auth functions
     */
    var skipIfLoggedIn = ['$q', '$auth', function($q, $auth) {
      var deferred = $q.defer();
      if ($auth.isAuthenticated()) {
        deferred.reject();
      } else {
        deferred.resolve();
      }
      return deferred.promise;
    }];

    var loginRequired = ['$q', '$location', '$auth', function($q, $location, $auth) {
      var deferred = $q.defer();
      if ($auth.isAuthenticated()) {
        deferred.resolve();
      } else {
        $location.path('/login');
      }
      return deferred.promise;
    }];

    $stateProvider
	  .state('location', {
	      url: '/',
          views: {
            	main: {
                    templateUrl: '/partials/location.html',
                    controller: 'LocationCtrl'
            	},
	        	contact: {
	                templateUrl: '/partials/contact.html',
	                controller: 'ContactCtrl'
	        	}
          },
	      resolve: {
	        loginRequired: loginRequired
	      }
	  })
      .state('login', {
        url: '/login',
        views: {
        	main: {
		        templateUrl: 'partials/login.html',
		        controller: 'LoginCtrl'
        	}
        },
        resolve: {
          skipIfLoggedIn: skipIfLoggedIn
        }
      })
      .state('loginX', {
        url: '/loginX',
        views: {
        	main: {
		        templateUrl: 'partials/loginX.html',
		        controller: 'LoginCtrl'
        	}
        },
        resolve: {
          skipIfLoggedIn: skipIfLoggedIn
        }
      })
      .state('signup', {
        url: '/signup',
        views: {
        	main: {
		        templateUrl: 'partials/signup.html',
		        controller: 'SignupCtrl'
        	}
        },
        resolve: {
          skipIfLoggedIn: skipIfLoggedIn
        }
      })
      .state('logout', {
        url: '/logout',
        views: {
        	main: {
        		template: null,
        		controller: 'LogoutCtrl'
        	}
        }
      })
      .state('profile', {
        url: '/profile',
        views: {
        	main: {
                templateUrl: 'partials/profile.html',
                controller: 'ProfileCtrl'
        	}
        },
        resolve: {
          loginRequired: loginRequired
        }
      });

    $urlRouterProvider.otherwise('/');

    /**
     *  Satellizer config
     */
    $authProvider.facebook({
      clientId: '1437600756316443'
    });

    $authProvider.google({
      clientId: '521255295329-s5tukj28ic2f3monckb9cbhsgjmb8703.apps.googleusercontent.com'
    });

    $authProvider.github({
      clientId: 'YOUR_GITHUB_CLIENT_ID'
    });

    $authProvider.linkedin({
      clientId: 'YOUR_LINKEDIN_CLIENT_ID'
    });

    $authProvider.instagram({
      clientId: 'YOUR_INSTAGRAM_CLIENT_ID'
    });

    $authProvider.yahoo({
      clientId: 'YOUR_YAHOO_CLIENT_ID'
    });

    $authProvider.live({
      clientId: 'YOUR_MICROSOFT_CLIENT_ID'
    });

    $authProvider.twitch({
      clientId: 'YOUR_TWITCH_CLIENT_ID'
    });

    $authProvider.bitbucket({
      clientId: 'YOUR_BITBUCKET_CLIENT_ID'
    });

    $authProvider.spotify({
      clientId: 'YOUR_SPOTIFY_CLIENT_ID'
    });

    $authProvider.twitter({
      url: '/auth/twitter'
    });

    $authProvider.oauth2({
      name: 'foursquare',
      url: '/auth/foursquare',
      clientId: 'MTCEJ3NGW2PNNB31WOSBFDSAD4MTHYVAZ1UKIULXZ2CVFC2K',
      redirectUri: window.location.origin || window.location.protocol + '//' + window.location.host,
      authorizationEndpoint: 'https://foursquare.com/oauth2/authenticate'
    });
  });
