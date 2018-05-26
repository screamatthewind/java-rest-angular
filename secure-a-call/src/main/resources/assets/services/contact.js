angular.module('MyApp')
  .factory('Contact', function($http) {
    return {
      getAll: function(locationId) {
        return $http.get('/api/contact/all/' + locationId);
      },
      get: function(id) {
          return $http.get('/api/contact/' + id);
        },      
      save: function(contactData) {
    	return $http.post('/api/contact', contactData);  
      },
/*      update: function(contactData) {
        return $http.put('/api/contact', contactData);
      },*/
      delete: function(id) {
          return $http.delete('/api/contact/' + id);
       }
    };
  });
