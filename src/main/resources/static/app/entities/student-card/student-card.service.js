(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('StudentCard', StudentCard);

      StudentCard.$inject = ['$http', 'Upload'];

      function StudentCard ($http, Upload) {
           return{
              getAll: function(anneeId){
                    return $http.get("/api/student-cards-by-annee/"+anneeId);
              },
              save: function(studentCard, recto, verso, onSaveSuccess, onSaveError){
                   return  Upload.upload({
                        url: '/api/student-cards',
                        method: 'POST',
                        data: {recto: recto,
                               verso: verso,
                              'studentCard': Upload.jsonBlob(studentCard) },
                        arrayKey: '',
                    }).then(function(response){
                        onSaveSuccess(response);
                      }, function errorCallback(response) {
                       onSaveError(response);
                      });
              },
              update: function(studentCard, recto, verso, onSaveSuccess, onSaveError){
                   return  Upload.upload({
                        url: '/api/student-cards',
                        method: 'PUT',
                        data: {recto: recto,
                               verso: verso,
                              'studentCard': Upload.jsonBlob(studentCard) },
                        arrayKey: '',
                    }).then(function(response){
                        onSaveSuccess(response);
                      }, function errorCallback(response) {
                       onSaveError(response);
                      });
              },
              get: function(id){
                    return $http.get("/api/student-cards/" + id);
              },

              getImage: function(anneeId, name){
                    return $http.get("api/student-card-image/" + anneeId +"?name=" + name, {responseType: "arraybuffer"})
              },

              delete: function(id){
                    return $http.delete("/api/student-cards/" + id);
              }

           };
      }
})();