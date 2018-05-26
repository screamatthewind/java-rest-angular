angular.module('MyApp')
  .factory('Location', function($http) {
    return {
      getLocations: function() {
        return $http.get('/api/location/all');
      },
      getLocation: function(id) {
          return $http.get('/api/location/' + id);
      },
      saveLocation: function(locationData) {
			return $http.post('/api/location/', locationData);
      },
      updateLocation: function(locationData) {
			return $http.put('/api/location/', locationData);
      },
      deleteLocation: function(id) {
          return $http.delete('/api/location/' + id);
      }
   };
});