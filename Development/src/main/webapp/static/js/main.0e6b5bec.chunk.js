(this["webpackJsonptaut-app"]=this["webpackJsonptaut-app"]||[]).push([[0],{22:function(e,t,n){},39:function(e,t,n){e.exports=n(70)},44:function(e,t,n){},70:function(e,t,n){"use strict";n.r(t);var a=n(0),o=n.n(a),s=n(34),c=n.n(s),r=(n(44),n(4)),l=n(5),i=n(7),u=n(6),E=n(8),m=n(12),S=n(11),h=(n(22),n(14)),f={LOGIN:"LOGIN",USER_CREATE:"USER_CREATE",SEARCH_USERS_BY_NAME:"SEARCH_USERS_BY_NAME",FRIEND_USER:"FRIEND_USER"};function p(e){e.contentType==f.LOGIN&&function(e){e.content.includes(b.SUCCESS)?(console.log("SUCCESS: User successfully logged into session."),alert("User successfully logged into session.")):e.content.includes(b.FAILURE)&&(console.log("ERROR: User Name Does not Exist. Please Create User First."),alert("User Name Does not Exist. Please Create User First."))}(e),e.contentType==f.USER_CREATE&&function(e){e.content.includes(b.SUCCESS)?(console.log("User Successfully Created!"),alert("User Successfully Created!")):e.content.includes(b.FAILURE)&&(console.log("ERROR: Could Not Create User Name. Please Try Another Name."),alert("This User Name is already taken. Please Try Another Name."))}(e),e.contentType,f.SEARCH_USERS_BY_NAME}var g,d={USER_SERVICE:"USER_SERVICE",SESSION_SERVICE:"SESSION_SERVICE",GROUP_SERVICE:"GROUP_SERVICE",GROUP_MESSAGE:"GROUP_MESSAGE",DIRECT_MESSAGE:"DIRECT_MESSAGE",BROADCAST_MESSAGE:"BROADCAST_MESSAGE"},b={SUCCESS:"SUCCESS",FAILURE:"FAILURE"};var C=!1;function v(){if(console.log("Checking Socket Connection"),"undefined"===typeof g){console.log("Connecting to Socket");document.location.host,document.location.pathname;(g=new WebSocket("ws://localhost:8080/prattle/chat/")).onopen=function(){console.log("Connection open!"),C=!0},g.onclose=function(e){console.log("websocket closing. Code:",e),C=!1},g.onerror=function(e){console.log("Websocket Error"),console.log("Error Code: ",e.data)},g.onmessage=function(e){var t;console.log("Received Message"),console.log(e.data),(t=JSON.parse(e.data)).contentType,d.BROADCAST_MESSAGE,t.from==d.USER_SERVICE&&p(t)}}}function O(e){C?g.send(e):(v(),setTimeout((function(){g.send(e)}),500))}var y=function(e){function t(e){var n;return Object(r.a)(this,t),(n=Object(i.a)(this,Object(u.a)(t).call(this,e))).state={value:""},n.handleChange=n.handleChange.bind(Object(h.a)(n)),n.handleLogin=n.handleLogin.bind(Object(h.a)(n)),n.handleCreate=n.handleCreate.bind(Object(h.a)(n)),n}return Object(E.a)(t,e),Object(l.a)(t,[{key:"handleChange",value:function(e){this.setState({value:e.target.value})}},{key:"handleLogin",value:function(e){e.preventDefault(),function(e){var t=JSON.stringify({type:d.USER_SERVICE,contentType:f.LOGIN,content:e});console.log(t),O(t)}(this.state.value)}},{key:"handleCreate",value:function(e){var t;e.preventDefault(),t=this.state.value,O(JSON.stringify({type:d.USER_SERVICE,contentType:f.USER_CREATE,content:t}))}},{key:"render",value:function(){return o.a.createElement("div",{className:"section is-fullheight"},o.a.createElement("div",{className:"container"},o.a.createElement("div",{className:"column is-4 is-offset-4"},o.a.createElement("div",{className:"box"},o.a.createElement("form",{onSubmit:this.handleSubmit},o.a.createElement("div",{className:"field"},o.a.createElement("label",{className:"label"},"User Name"),o.a.createElement("div",{className:"control"},o.a.createElement("input",{className:"input",type:"text",name:"username",onChange:this.handleChange}))),o.a.createElement("div",{className:"buttons is-grouped"},o.a.createElement("button",{type:"button",className:"button is-block is-info is-fullwidth",onClick:this.handleLogin},"Login"),o.a.createElement("button",{type:"button",className:"button is-block is-info is-fullwidth",onClick:this.handleCreate},"Create")))))))}}]),t}(o.a.Component);function R(){return o.a.createElement("header",null,o.a.createElement("h1",null,"TautChat"),o.a.createElement(m.b,{to:"/"},"Home")," | "," ",o.a.createElement(m.b,{to:"/about"},"About")," | "," ",o.a.createElement(m.b,{to:"/chat"},"Chat")," | "," ",o.a.createElement(m.b,{to:"/groupchat"},"GroupChat"))}function U(){return o.a.createElement(o.a.Fragment,null,o.a.createElement("h1",null,"About"),o.a.createElement("p",null,"This is a to do list app v0.0.1"))}var A=n(38),N=function(e){function t(){var e,n;Object(r.a)(this,t);for(var a=arguments.length,o=new Array(a),s=0;s<a;s++)o[s]=arguments[s];return(n=Object(i.a)(this,(e=Object(u.a)(t)).call.apply(e,[this].concat(o)))).getStyle=function(){return{background:"#f4f4f4",padding:"10px",borderBottom:"1px #ccc dotted"}},n}return Object(E.a)(t,e),Object(l.a)(t,[{key:"render",value:function(){var e=this.props.message,t=e.from,n=e.to,a=e.content;return o.a.createElement("div",{style:this.getStyle()},o.a.createElement("p",null,t,"->",n,": ",a))}}]),t}(a.Component),j=n(17),_=n.n(j),I=n(18),M=n.n(I),k=n(37),T=function(e){function t(){var e,n;Object(r.a)(this,t);for(var a=arguments.length,o=new Array(a),s=0;s<a;s++)o[s]=arguments[s];return(n=Object(i.a)(this,(e=Object(u.a)(t)).call.apply(e,[this].concat(o)))).state={content:""},n.onChange=function(e){return n.setState(Object(k.a)({},e.target.name,e.target.value))},n.onSubmit=function(e){e.preventDefault(),n.props.addMessage(n.state.content)},n}return Object(E.a)(t,e),Object(l.a)(t,[{key:"render",value:function(){return o.a.createElement("form",{onSubmit:this.onSubmit,style:{display:"flex"}},o.a.createElement("input",{type:"text",name:"content",placeholder:"Type Message Here",style:{flex:"10",padding:"5px"},value:this.state.content,onChange:this.onChange}),o.a.createElement("input",{type:"submit",value:"Submit",className:"btn",style:{flex:"1"}}))}}]),t}(a.Component),G=function(e){function t(){return Object(r.a)(this,t),Object(i.a)(this,Object(u.a)(t).apply(this,arguments))}return Object(E.a)(t,e),Object(l.a)(t,[{key:"render",value:function(){var e=this;return this.props.messages.map((function(t){return o.a.createElement(N,{message:t,filterMessages:e.filterMessages})}))}}]),t}(a.Component),D={ASCII:"ASCII",GROUP_MESSAGE:"GROUP_MESSAGE"};var x=function(e){function t(){var e,n;Object(r.a)(this,t);for(var a=arguments.length,o=new Array(a),s=0;s<a;s++)o[s]=arguments[s];return(n=Object(i.a)(this,(e=Object(u.a)(t)).call.apply(e,[this].concat(o)))).state={messages:[]},n.filterMessages=function(e,t){console.log(n.state.messages);var a=n.state.messages.filter((function(n){return n.from===e||n.from===t})).filter((function(n){return n.to===e||n.to===t}));console.log(a),n.setState({messages:a})},n.addMessage=function(e){var t=n.props.match.params,a={content:e,contentType:"DIRECT_MESSAGE",from:t.currentUser,to:t.messageWith,key:M.a.v4()};n.setState({messages:[].concat(Object(A.a)(n.state.messages),[a])}),function(e){var t=JSON.stringify({type:d.DIRECT_MESSAGE,from:e.from,to:e.to,contentType:D.ASCII,content:e.content});console.log(t),O(t)}(a)},n}return Object(E.a)(t,e),Object(l.a)(t,[{key:"componentDidMount",value:function(){this.loadPersistentMessages()}},{key:"loadPersistentMessages",value:function(){var e=this,t=this.props.match.params,n=t.currentUser,a=t.messageWith;console.log("Current user is: %s",n),console.log("Current friend is: %s",a),_.a.get("/../exampleJSON.json").then((function(t){return e.setState({messages:t.data},(function(){console.log(this.state),this.filterMessages(n,a)}))}))}},{key:"render",value:function(){return o.a.createElement(o.a.Fragment,null,o.a.createElement(G,{messages:this.state.messages}),o.a.createElement(T,{addMessage:this.addMessage}))}}]),t}(a.Component),L=function(e){function t(){return Object(r.a)(this,t),Object(i.a)(this,Object(u.a)(t).apply(this,arguments))}return Object(E.a)(t,e),Object(l.a)(t,[{key:"render",value:function(){return o.a.createElement(m.a,{basename:"prattle"},o.a.createElement("div",{className:"App"},o.a.createElement("div",{className:"container"},o.a.createElement(R,null),o.a.createElement(S.a,{exact:!0,path:"/",component:y}),o.a.createElement(S.a,{path:"/about",component:U}),o.a.createElement(S.a,{path:"/chat/:currentUser?/:messageWith?",component:x}))))}}]),t}(a.Component);n(69);c.a.render(o.a.createElement(L,null),document.getElementById("root"))}},[[39,1,2]]]);
//# sourceMappingURL=main.0e6b5bec.chunk.js.map