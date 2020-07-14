(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('Annee', Annee);

      Annee.$inject = ['$http', 'Upload'];

      function Annee ($http, Upload) {
           return{
              getAll: function(){
                        return $http.get("/api/annees");
              },
              save: function(annee, onSaveSuccess, onSaveError){
                  return $http.post("/api/annees", annee)
                          .then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
              },
              /*update: function(annee, onSaveSuccess, onSaveError){
                  return $http.put("/api/annees", annee).
                          then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
              },*/
              update: function(annee, logo, cachet, onSaveSuccess, onSaveError){
                 return  Upload.upload({
                      url: '/api/annees',
                      method: 'PUT',
                      data: {logo: logo,
                             cachet: cachet,
                            'annee': Upload.jsonBlob(annee) },
                      arrayKey: '',
                  }).then(function(response){
                      onSaveSuccess(response);
                    }, function errorCallback(response) {
                     onSaveError(response);
                    });
              },
              get: function(id){
                    return $http.get("/api/annees/" + id);
              },
              getActive: function(){
                  return $http.get("/api/annee-active");
              },
              setActive: function(id){
                  return $http.get("/api/annee-set-active/" + id);
              },
              getImage: function(anneeId, name){
                  return $http.get("api/annee-image/" + anneeId +"?name=" + name, {responseType: "arraybuffer"})
              },
              delete: function(id){
                    return $http.delete("/api/annees/" + id);
              }
           };
      }
})();