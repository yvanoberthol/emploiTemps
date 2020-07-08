(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('ReglementDetailController', ReglementDetailController);

    ReglementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Reglement'];

    function ReglementDetailController($scope, $rootScope, $stateParams, previousState, entity, Reglement) {
        var vm = this;
        vm.reglement = entity;
        console.log(vm.reglement)

        vm.previousState = previousState.name;

        vm.download = function(){
            Reglement.getJustificatif(vm.reglement.id)
            .then(function(response){
                showDownloadPopup(response);
            });
        }

        function showDownloadPopup(response){
           var filename = "reglement_" + vm.reglement.student.trim().replace(/ /g,"_") + ".png";
           var linkElement = document.createElement('a');
           try {
               var arrayBufferView = new Uint8Array( response.data );
               var blob = new Blob( [ arrayBufferView ], { type: "image/png" } );
               //var blob = new Blob([response.data], { type: contentType });
               var url = window.URL.createObjectURL(blob);
               linkElement.setAttribute('href', url);
               linkElement.setAttribute("download", filename);
               var clickEvent = new MouseEvent("click", {
                   "view": window,
                   "bubbles": true,
                   "cancelable": false
               });
               linkElement.dispatchEvent(clickEvent);
           } catch (ex) {
               console.log(ex);
           }
        }

        //=====================================
        function getFileNameFromHttpResponse(httpResponse) {
              var contentDispositionHeader = httpResponse.headers('Content-Disposition');
              var result = contentDispositionHeader.split(';')[1].trim().split('=')[1];
              return result.replace(/"/g, '');
        }
    }
})();
