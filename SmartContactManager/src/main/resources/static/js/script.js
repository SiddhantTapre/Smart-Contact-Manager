/*const toggleSidebar =() =>{
	
	if($(".sidebar").is(":visible")){
		$(".sidebar").css("display","none");
		$(".content").css("margin-left","0%");
	}else{
		$(".sidebar").css("display","block");
		$(".content").css("margin-left","20%");
	}
}*/
function toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    const content = document.querySelector('.content');
    sidebar.classList.toggle('active');

    if (sidebar.classList.contains('active')) {
        content.style.marginLeft = '0'; // Sidebar is closed, reset margin
    } else {
        content.style.marginLeft = '18%'; // Sidebar is open, apply margin
    }
}



const search=() => {



    let query = $("#search-input").val();

    if (query == "") {
        $(".search-result").hide();
    } else {
        console.log(query);

        // Correct the template literal syntax by using backticks and ${} for variable interpolation
        let url = `http://localhost:8999/search/${query}`;

        fetch(url)
        .then((response) => {
            return response.json();
        })
        .then((data) => {
            // Initialize HTML structure for search results
            let text = `<div class='list-group'>`;

            // Loop through data and generate list items for each contact
            data.forEach(contact => {
                text += `<a href='/user/${contact.c_id}/contact/' class='list-group-item list-group-item-action'> ${contact.name} </a>`;
            });

            text += `</div>`;

            // Populate and show search results
            $(".search-result").html(text);
            $(".search-result").show();
        });
    }
};

      