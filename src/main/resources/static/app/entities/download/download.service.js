(function() {
    'use strict';

    angular
        .module('myApp')
        .factory('DownloadService', DownloadService);

    DownloadService.$inject = ['$uibModal', '$http'];

    function DownloadService ($uibModal, $http) {
        var service = {
            openOneBulletin: openOneBulletin,
            openAllBulletins: openAllBulletins,
            openStudentCards: openStudentCards,
            getOneBulletin: getOneBulletin,
            getAllBulletins: getAllBulletins,
            getStudentCards: getStudentCards,
            download: download
        };

        var modalInstance = null;
        var resetModal = function () {
            modalInstance = null;
        };
        return service;

        function openAllBulletins (promoId, trimestreId) {
            if (modalInstance !== null) return;
            console.log("bulletin open")

            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/entities/download/pdf-dialog.html',
                controller: 'DownloadController',
                controllerAs: 'vm',
                backdrop: 'false',
                size: 'lg',
                resolve: {
                    downloadResp: [ 'DownloadService', 'session',  function(DownloadService, session) {
                         return DownloadService.getAllBulletins(promoId, session.getAnneeId(), trimestreId);
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });

            modalInstance.result.then(
                resetModal,
                resetModal
            );
        }

        function openOneBulletin (student, trimestreId) {
            if (modalInstance !== null) return;
            console.log("bulletin open")

            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/entities/download/pdf-dialog.html',
                controller: 'DownloadController',
                controllerAs: 'vm',
                backdrop: 'false',
                size: 'lg',
                resolve: {
                    downloadResp: [ 'DownloadService', 'session',  function(DownloadService, session) {
                         return DownloadService.getOneBulletin(student.id, session.getAnneeId(), trimestreId);
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });

            modalInstance.result.then(
                resetModal,
                resetModal
            );
        }

         function openStudentCards (promoId) {
            if (modalInstance !== null) return;
            console.log("student cards open")

            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/entities/download/pdf-dialog.html',
                controller: 'DownloadController',
                controllerAs: 'vm',
                backdrop: 'false',
                size: 'lg',
                resolve: {
                    downloadResp: [ 'DownloadService',  function(DownloadService) {
                         return DownloadService.getStudentCards(promoId);
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });

            modalInstance.result.then(
                resetModal,
                resetModal
            );
        }

        function download (downloadRequest) {
            return $http.post("/api/download/", downloadRequest, {responseType: 'arraybuffer'})
        }

        function getOneBulletin(studentId, anneeId, trimestreId){
            return $http.get("/api/bulletin?student=" + studentId + "&annee=" + anneeId+ "&trimestre=" + trimestreId,
                            {responseType: 'arraybuffer'});
        }

        function getAllBulletins(promoId, anneeId, trimestreId){
            return $http.get("/api/bulletins?promo=" + promoId + "&annee=" + anneeId+ "&trimestre=" + trimestreId,
                            {responseType: 'arraybuffer'});
        }

        function getStudentCards(promoId){
            return $http.get("/api/student-cards?promo=" + promoId,
                            {responseType: 'arraybuffer'});
        }
    }
})();
