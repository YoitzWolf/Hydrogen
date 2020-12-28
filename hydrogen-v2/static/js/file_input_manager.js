
function get_label_setter(input, label){

	function deco(){
		label.textContent = input.value.split("\\")[2];
	}

	return deco;

}


function register_listener(input, label) {
	input.addEventListener("change", get_label_setter(input, label));
}