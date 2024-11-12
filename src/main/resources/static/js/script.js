console.log("Hello")

function toggleSidebar() {
    
    const sidebar =  document.getElementsByClassName("sidebar")[0];
    const content =  document.getElementsByClassName("content")[0];

    if(window.getComputedStyle(sidebar).display === "none"){
        sidebar.style.display = "block";
        content.style.marginLeft = "20%";
    }
    else{
        sidebar.style.display = "none";
        content.style.marginLeft = "0%";
    }
}

const search=() =>{

    let query = $("#search-input").val();

    if(query==""){
        $(".search-result").hide();

    }
    else{
        console.log(query);

        //sending request to the user

        let url=`http://localhost:8082/search/${query}`;

        fetch(url)
        .then((response)=>{
            return response.json();
        })
        .then((data)=>{
            console.log(data);
            let text=`<div class='list-group'>`;

            data.forEach((contactDetails)=>{
                text +=`<a href='/user/${contactDetails.cId}/contact' class='list-group-item list-group-item-action'>${contactDetails.firstName} </a>`;
            });

            text +=`</div>`;

            $(".search-result").html(text);
            $(".search-result").show();

        });

       
    }
};