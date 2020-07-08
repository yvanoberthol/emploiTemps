(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('BulletinController', BulletinController);

    BulletinController.$inject = ['$rootScope', '$scope', '$http', 'auth', 'session', '$sce', '$uibModalInstance',
                                'student', 'BulletinService', '$filter'];

    function BulletinController ($rootScope, $scope, $http, auth, session, $sce, $uibModalInstance,
                                student, BulletinService, $filter) {
        var vm = this;
        vm.clear = clear;
        vm.student = student;
        if(vm.student.dateNaissance)
        vm.student.dateNaissance = moment(vm.student.dateNaissance, "DDMMYYYY").format("DD/MM/YYYY")
        //vm.config = config;
        //console.log(vm.config.logo)
        //vm.url = "data:image/png;base64," + vm.config.logo;
        //vm.config.bulletin = $sce.trustAsHtml(vm.config.bulletin);

        console.log(vm.student)

        function clear () {
           $uibModalInstance.dismiss();
        }

        vm.tabSelected = tabSelected;
        function tabSelected(index){
            vm.selectedTab = index;
            vm.ligneTrimestre = vm.student.notes[index];
            console.log(vm.ligneTrimestre)
        }

        tabSelected(0);


        vm.getNote = function(sequenceId, ligneMatiere){
           var studentNote = $filter('filter')(ligneMatiere.notes, {sequenceId: sequenceId})[0];
           return studentNote.note;
        }

        vm.download = download;
        function download(){
            vm.loading = true;
            var name = vm.ligneTrimestre.classe + "_" + vm.student.name+ "_" + vm.ligneTrimestre.trimestre;
            vm.content = angular.element($('#doc')).html();
            var downloadRequest = {
                'content': vm.content,
                'filename' : name
            };
            //console.log(downloadRequest);
            BulletinService.download(downloadRequest)
            .then(function(response){
                showDownloadPopup(response);
                //vm.modalInstance.dismiss();
                vm.loading = false;
            });
        };

        function showDownloadPopup(response){
           var headers = response.headers();
           var filename = getFileNameFromHttpResponse(response);
           var contentType = headers['content-type'];
           var linkElement = document.createElement('a');

           try {
               var blob = new Blob([response.data], { type: contentType });
               var url = window.URL.createObjectURL(blob);
               //window.open(url);

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
              return result.replace("/g", '');
        }
    }
})();
