(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('DownloadController', DownloadController);

    DownloadController.$inject = ['$rootScope', '$scope', '$http', 'auth', 'session', '$uibModalInstance',
                                 'downloadResp', '$filter'];

    function DownloadController ($rootScope, $scope, $http, auth, session, $uibModalInstance,
                                 downloadResp, $filter) {
        var vm = this;
        vm.clear = clear;
        vm.downloaded = new Uint8Array(downloadResp.data);

        function clear () {
           $uibModalInstance.dismiss();
        }

        vm.showDownloadPopup = showDownloadPopup;
        function showDownloadPopup(){
           var headers = downloadResp.headers();
           var filename = getFileNameFromHttpResponse(downloadResp);
           var contentType = headers['content-type'];
           var linkElement = document.createElement('a');
           try {
               var blob = new Blob([downloadResp.data], { type: contentType });
               var url = window.URL.createObjectURL(blob);
               linkElement.setAttribute('href', url);
               linkElement.setAttribute("download", filename);
               var clickEvent = new MouseEvent("click", {
                   "view": window,
                   "bubbles": true,
                   "cancelable": false
               });
               linkElement.dispatchEvent(clickEvent);
               $uibModalInstance.dismiss();
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
