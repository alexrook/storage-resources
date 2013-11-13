'use strict';

/* Services */

angular.module('sres.services', []).
    factory('shared',function(){
        
        var reportTypes=[],
            listeners={},
            user={},
            report={},
            state='';
        
        function callListeners(event,data) {
            if (listeners[event]) {
                   for (var i=0;i<listeners[event].length;i++){
                    listeners[event][i](data);
                }
            }
        }
        
        return {
            addListener:function(listener,event){
                var listenersForEvent=listeners[event]||(listeners[event]=[]);
                listenersForEvent.push(listener);
            },
            setReportTypes:function (data) {
                reportTypes = data;
                console.log(data);
                callListeners('reporttypes',data);
                
            },
            getReportTypes:function () {
                return reportTypes;
            },
            setUser:function(data){
                user=data;
                console.log(data);
                callListeners('user',data);
            },
            getUser:function(){
                return user;
            },
            setReport:function(data){
                report=data;
                console.log(data);
                callListeners('report',data);
            },
            getReport:function(){
                return report;
            },
            setState:function(data){
                state=data;
                console.log(data);
                callListeners('state',data);
            },
            getState:function(data){
                return state;
            }
        };    
        
    });