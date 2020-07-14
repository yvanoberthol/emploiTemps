(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('PromoNotesSummaryController', PromoNotesSummaryController);

    PromoNotesSummaryController.$inject = ['$scope', '$state', '$stateParams', 'previousState', 'entity', 'session',
                                    'Student', 'NgTableParams', 'Trimestre', '$filter', 'DownloadService'];

    function PromoNotesSummaryController($scope, $state, $stateParams, previousState, entity, session,
                                    Student, NgTableParams, Trimestre, $filter, DownloadService) {
        var vm = this;
        vm.promo = entity;
        console.log(vm.promo)

        vm.previousState = previousState.name;
        vm.name = "";
        vm.trimestreId = "";
        vm.trimestres = [];

        Trimestre.getByAnnee(session.getAnneeId())
        .then(function(response){
            vm.trimestres = response.data;
        })

        var initialParams = {
            count: 200, // initial page size
            sorting: { 'finalNote': "desc" }
        };

        function loadStudentsWithNotes(){
            Student.getByPromoAndTrimestreIdWithNotes(vm.name, vm.promo.id, vm.trimestreId)
            .then(function(response){
                vm.loading = false;
                vm.students = response.data;
                vm.tableParams = new NgTableParams(initialParams, {
                    counts: [],
                    // page size buttons (right set of buttons in demo)
                    //counts: [10,25,50,100],
                    //filterDelay: 1000,
                    // determines the pager buttons (left set of buttons in demo)
                    paginationMaxBlocks: 10,
                    paginationMinBlocks: 2,
                    dataset: vm.students
                });
            })
        }
        loadStudentsWithNotes();

        vm.noteChanged = function(){
           loadStudentsWithNotes();
           vm.tableParams.reload();
        }

        vm.openBulletinModal = function(student, trimestreId){
            /*
            vm.loading = true;
            DownloadService.getBulletin(student.id, session.getAnneeId(), trimestreId)
            .then(function(response){
                showDownloadPopup(response);
                vm.loading = false;
            });
            */
            DownloadService.openOneBulletin(student, trimestreId);
        }

        vm.openAll = function(trimestreId){
           DownloadService.openAllBulletins(vm.promo.id, trimestreId);
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
