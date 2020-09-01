(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('PromoNotesImportController', PromoNotesImportController);

    PromoNotesImportController.$inject = ['$timeout', '$rootScope', '$scope', '$state', '$stateParams', '$uibModalInstance', 'Promo', 'DownloadService'];

    function PromoNotesImportController ($timeout, $rootScope, $scope, $state, $stateParams, $uibModalInstance, Promo, DownloadService) {
        var vm = this;

        vm.promoId = $stateParams.promoId;
        vm.sequenceId = $stateParams.sequenceId;
        vm.matiereId = $stateParams.matiereId;

        vm.clear = clear;
        vm.cancel = cancel;

        function clear () {
             $uibModalInstance.close(true);
        }
        function cancel () {
            $uibModalInstance.dismiss();
        }

        vm.import = function(){
            vm.isSaving = true;
            vm.loading = true;
            Promo.importNotes(vm.file, vm.promoId, vm.sequenceId, vm.matiereId, onSuccess, onError);
        }

        function onSuccess(response) {
            $rootScope.$broadcast('importSuccess', response.data);
            $uibModalInstance.close();
            vm.isSaving = false;
            vm.loading = false;
        }

        function onError () {
            vm.isSaving = false;
            vm.loading = false;
        }

        vm.downloadSample = function(){
            vm.loading = true;
            DownloadService.getNotesImportSample(vm.promoId, vm.sequenceId)
            .then(function(response){
                showDownloadPopup(response);
                vm.loading = false;
            });
        }

        function showDownloadPopup(response){
           var headers = response.headers();
           var filename = getFileNameFromHttpResponse(response);
           var contentType = headers['content-type'];
           var linkElement = document.createElement('a');
           try {
               var blob = new Blob([response.data], { type: contentType });
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
