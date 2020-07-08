(function() {
'use strict';

angular
.module('myApp')
.controller('NavbarController', NavbarController);

NavbarController.$inject = ['$rootScope', '$scope', '$state', 'LoginService', 'session', 'auth',
                            'JhiLanguageService', '$translate', 'tmhDynamicLocale',
                            'AnneeChangeService', 'Annee', '$location'];

function NavbarController ($rootScope, $scope, $state, LoginService, session, auth,
                            JhiLanguageService, $translate, tmhDynamicLocale,
                            AnneeChangeService, Annee, $location) {
    var vm = this;
    //vm.login = LoginService.open;
    vm.changeAnnee = AnneeChangeService.open;

    vm.changeLanguage = changeLanguage;
    vm.languages = null;

    if(auth.isAuthenticated()){
        Annee.getActive().then(function(response){
            vm.activeAnnee = response.data;
            if(!session.getAnnee())
            session.setAnnee(vm.activeAnnee);
        })
    }


    JhiLanguageService.getAll().then(function (languages) {
        vm.languages = languages;
    });

    function changeLanguage (languageKey) {
        $translate.use(languageKey);
        tmhDynamicLocale.set(languageKey);
        session.setLangKey(languageKey);
    }

    JhiLanguageService.getCurrent().then(function(current) {
        var lang = current;

        if(!lang)
        lang = "fr";

        if (lang !== current) {
           $translate.use(lang);
        }
        session.setLangKey(lang);
    });

    $scope.isActive = function (viewLocation) {
        return viewLocation === $location.path();
    };
}
})();