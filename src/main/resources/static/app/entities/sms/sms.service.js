(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('Sms', Sms);

      Sms.$inject = ['$http'];

      function Sms ($http) {
           return{
              getAll: function(page, size, sortBy, direction, createdDateFrom, createdDateTo, sender, recipient){
                        return $http.get("/api/sms"
                                            +"?page=" + page
                                            + "&size=" + size
                                            + "&sortBy=" + sortBy
                                            + "&direction=" + direction
                                            + "&createdDateFrom=" + createdDateFrom
                                            + "&createdDateTo=" + createdDateTo
                                            + "&sender=" + sender
                                            + "&recipient=" + recipient
                                            );
              },
              save: function(sms, onSaveSuccess, onSaveError){
                  return $http.post("/api/sms", sms)
                          .then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
              },
              update: function(sms, onSaveSuccess, onSaveError){
                  return $http.put("/api/sms", sms).
                          then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
              },
              get: function(id){
                    return $http.get("/api/sms/" + id);
              },
              delete: function(id){
                    return $http.delete("/api/sms/" + id);
              }
           };
      }
})();