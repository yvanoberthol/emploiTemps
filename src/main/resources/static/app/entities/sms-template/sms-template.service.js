(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('SmsTemplate', SmsTemplate);

      SmsTemplate.$inject = ['$http'];

      function SmsTemplate ($http) {
           return{
            save: function(smsTemplate, onSaveSuccess, onSaveError){
                    return $http.post("/api/sms-templates", smsTemplate)
                          .then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
                },
                update: function(smsTemplate, onSaveSuccess, onSaveError){
                    return $http.put("/api/sms-templates", smsTemplate).
                          then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
                },
              getAll: function(page, size, sortBy, direction){
                        return $http.get("/api/sms-templates");
              },
              get: function(id){
                    return $http.get("/api/sms-templates/" + id);
              }
           };
      }
})();