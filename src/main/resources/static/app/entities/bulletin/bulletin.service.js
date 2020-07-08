(function() {
    'use strict';

    angular
        .module('myApp')
        .factory('BulletinService', BulletinService);

    BulletinService.$inject = ['$uibModal', '$http'];

    function BulletinService ($uibModal, $http) {
        var service = {
            open: open,
            openAll: openAll,
            getBulletin: getBulletin,
            getAll: getAll,
            download: download
        };

        var modalInstance = null;
        var resetModal = function () {
            modalInstance = null;
        };
        return service;

        function openAll (promoId, trimestreId) {
            if (modalInstance !== null) return;
            console.log("bulletin open")

            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/entities/bulletin/pdf-dialog.html',
                controller: 'BulletinController',
                controllerAs: 'vm',
                backdrop: 'false',
                size: 'lg',
                resolve: {
                    bulletin: [ 'BulletinService', 'session',  function(BulletinService, session) {
                         return BulletinService.getAll(promoId, session.getAnneeId(), trimestreId)
                         .then(function(response){
                            return response.data;
                        })
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

        function open (student, trimestreId) {
            if (modalInstance !== null) return;
            console.log("bulletin open")

            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/entities/bulletin/pdf-dialog.html',
                controller: 'BulletinController',
                controllerAs: 'vm',
                backdrop: 'false',
                size: 'lg',
                resolve: {
                    bulletin: [ 'BulletinService', 'session',  function(BulletinService, session) {
                         return BulletinService.getBulletin(student.id, session.getAnneeId(), trimestreId)
                         .then(function(response){
                            return response.data;
                        })
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

        function getBulletin(studentId, anneeId, trimestreId){
            return $http.get("/api/bulletin?student=" + studentId + "&annee=" + anneeId+ "&trimestre=" + trimestreId,
                            {responseType: 'arraybuffer'});
        }

        function getAll(promoId, anneeId, trimestreId){
            return $http.get("/api/bulletins?promo=" + promoId + "&annee=" + anneeId+ "&trimestre=" + trimestreId,
                            {responseType: 'arraybuffer'});
        }
    }
})();
