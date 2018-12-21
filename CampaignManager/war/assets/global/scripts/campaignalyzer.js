function confirmAccountDeleteSubmit(accountName) {
	var s = "By deleting this account ["  + accountName + "] you relinquish access to your campaigns and they can not be reinstated.\r\n"
	s += "Deleting your account will automatically cancel your subscription.\r\n";
	s += "We recommend you backup your campaigns using the \"Export\" feature and notify all users of the account termination.\r\n";
	s += "Are you sure you want to delete this account?\r\n";
	return confirm(s);
}

function confirmAccountDelete(delUrl, accountName) {
	if (confirmAccountDeleteSubmit(accountName)) {
		document.location = delUrl;
	}
}

function confirmCampaignDeleteSubmit() {
	return confirm("Are you sure you want to delete this campaign?");
}

function confirmCampaignDelete(delUrl) {
	if (confirmCampaignDeleteSubmit()) {
		document.location = delUrl;
	}
}

function confirmTaggedUrlDelete(delUrl) {
	if (confirm("Are you sure you want to delete this tagged URL?")) {
		document.location = delUrl;
	}
}

function confirmUserDelete(delUrl) {
	if (confirm("Are you sure you want to remove this user?")) {
		document.location = delUrl;
	}
}

function validateTaggingParametersForm() {
	var s = "";
	var x = document.forms["taggingParametersForm"]["campaignParam"].value;
	if (null==x || ""==x) {
		s += "Campaign cannot be empty!\r\n";
	}

	x = document.forms["taggingParametersForm"]["mediumParam"].value;
	if (null==x || ""==x) {
		s += "Medium cannot be empty!\r\n";
	}

	x = document.forms["taggingParametersForm"]["sourceParam"].value;
	if (null==x || ""==x) {
		s += "Source cannot be empty!\r\n";
	}

	x = document.forms["taggingParametersForm"]["termParam"].value;
	if (null==x || ""==x) {
		s += "Term cannot be empty!\r\n";
	}

	x = document.forms["taggingParametersForm"]["contentParam"].value;
	if (null==x || ""==x) {
		s += "Content cannot be empty!\r\n";
	}

	if (""!=s) {
		alert(s);
		return false;
	}
}

function validateAccountForm() {
	var s = "";
	var x = document.forms["accountForm"]["email"].value;
	if (null==x || ""==x) {
		s += "E-mail Address cannot be empty!\r\n";
	}

	x = document.forms["accountForm"]["fname"].value;
	if (null==x || ""==x) {
		s += "First Name cannot be empty!\r\n";
	}

	x = document.forms["accountForm"]["lname"].value;
	if (null==x || ""==x) {
		s += "Last Name cannot be empty!\r\n";
	}

	x = document.forms["accountForm"]["company"].value;
	if (null==x || ""==x) {
		s += "Company Name cannot be empty!\r\n";
	}

	x = document.forms["accountForm"]["name"].value;
	if (null==x || ""==x) {
		s += "Account Name cannot be empty!\r\n";
	}

	x = document.forms["accountForm"]["address"].value;
	if (null==x || ""==x) {
		s += "Address cannot be empty!\r\n";
	}

	x = document.forms["accountForm"]["city"].value;
	if (null==x || ""==x) {
		s += "City cannot be empty!\r\n";
	}

	x = document.forms["accountForm"]["state"].value;
	if (null==x || ""==x) {
		s += "State cannot be empty!\r\n";
	}

	x = document.forms["accountForm"]["zip"].value;
	if (null==x || ""==x) {
		s += "Zip/ Postal Code cannot be empty!\r\n";
	}

	x = document.forms["accountForm"]["country"].value;
	if (null==x || ""==x) {
		s += "Country cannot be empty!\r\n";
	}

	x = document.forms["accountForm"]["phone"].value;
	if (null==x || ""==x) {
		s += "Phone cannot be empty!\r\n";
	}

	x = document.forms["accountForm"]["website"].value;
	if (null==x || ""==x) {
		s += "Website cannot be empty!\r\n";
	}

	if (!document.forms["accountForm"]["agreement"].checked) {
		s += "You have to agree on Terms of Use and Privacy Policy!\r\n";
	}

	if (""!=s) {
		alert(s);
		return false;
	}
}

function validateCampaignForm() {
	var s = "";
	var x = document.forms["campaignForm"]["utm_campaign"].value;
	if (null==x || ""==x) {
		s += "Campaign cannot be empty!\r\n";
	}

	x = document.forms["campaignForm"]["utm_medium"].value;
	if (null==x || ""==x) {
		s += "Medium cannot be empty!\r\n";
	}

	x = document.forms["campaignForm"]["utm_source"].value;
	if (null==x || ""==x) {
		s += "Source cannot be empty\r\n";
	}

	if (""!=s) {
		alert(s);
		return false;
	}
}

function validateMobileCampaignForm() {
	var s = "";
	var x = document.forms["mobileCampaignForm"]["packageName"].value;
	if (null==x || ""==x) {
		s += "Package Name cannot be empty!\r\n";
	}
	
	 x = document.forms["mobileCampaignForm"]["utm_campaign"].value;
		if (null==x || ""==x) {
			s += "Campaign cannot be empty!\r\n";
		}

	x = document.forms["mobileCampaignForm"]["utm_medium"].value;
	if (null==x || ""==x) {
		s += "Medium cannot be empty!\r\n";
	}

	x = document.forms["mobileCampaignForm"]["utm_source"].value;
	if (null==x || ""==x) {
		s += "Source cannot be empty\r\n";
	}

	if (""!=s) {
		alert(s);
		return false;
	}
}

function validateTaggedUrlForm() {
	var x = document.forms["taggedUrlForm"]["utm_url"].value;
	if (null==x || ""==x) {
		alert("URL cannot be empty");
		return false;
	}
}

function validateUserForm() {
	var x = document.forms["userForm"]["newUserEmail"].value;
	if (null==x || ""==x) {
		alert("User e-mail cannot be empty!\r\n");
		return false;
	}
}

function validateChannelForm() {
	var act	= document.forms["channelForm"]["act"].value;

//	if (act.indexOf("Add")>-1) {
//		var ch	= document.forms["channelForm"]["availChannels"].value;
//	} else {
//		var ch	= document.forms["channelForm"]["selChannels"].value;
//	}
//
//	if (null==ch || ""==ch) {
//		alert("You must select/ enter at least one channel!");
//		return false;
//	}
}

function setDefaultParams() {
	document.forms["taggingParametersForm"]["campaignParam"].value = "utm_campaign";
	document.forms["taggingParametersForm"]["mediumParam"].value = "utm_medium";
	document.forms["taggingParametersForm"]["sourceParam"].value = "utm_source";
	document.forms["taggingParametersForm"]["termParam"].value = "utm_term";
	document.forms["taggingParametersForm"]["contentParam"].value = "utm_content";
	document.forms["taggingParametersForm"]["queryParam"].value = "";
}


function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}


function defautAvatar(){
	var src = $('#profilePhoto').attr('src');	
	if (src == "" || src == null) {
		$('#profilePhoto').attr("src","/images/default_avatar.png");
		$('#profilePhoton').attr("src","/images/default_avatar.png");
		} 	
}



function secondstotime(secs)
{
    var t = new Date(1970,0,1);
    t.setSeconds(secs);
    var s = t.toTimeString().substr(0,8);
    if(secs > 86399)
    	s = Math.floor((t - Date.parse("1/1/70")) / 3600000) + s.substr(2);
    return s;
}

