jQuery(document).ready(function($) {

	
	//$("input:text").placeholder();  	
	$('input[placeholder], textarea[placeholder]').placeholder();

	
	//custom dropdown
	if ($('#center-select').length){
		customSelect('center-select');
	}
	
	$('select').dropkick();
	
	//qTip
	$('.content-container p').find('a[title]').qtip({
		position: {
      my: 'bottom center',
			adjust: {
				y: -35,
				x: 20,
				mouse: false
      }
   }
	});
	
	//expandables
	$('.expandables .data').hide();
	
	$('.expandables-simple .trigger').click(function(e) {
		e.preventDefault();
		$(this).next('.data').slideToggle(200);
		$(this).toggleClass('open');
	});


	//tabs
	$(function() {
		$( "#tabs" ).tabs();
	});


	//fancybox+
	$('.gallery').each(function (index) {
			$('a', $(this)).attr('rel','gallery-'+index).fancybox({
				'overlayShow'	: true,
				'margin' : 30,
				'padding' : 0,
				'autoScale' : true,
				'overlayOpacity' : '0.85',
				'changeFade' : 200,
				'overlayColor' : '#000',
				'titleShow' : true,
				'titlePosition' : 'over',
				'titleFormat'		: function(title, currentArray, currentIndex, currentOpts) {
														return '<span id="fancybox-title-over">' + (currentIndex + 1) + ' / ' + currentArray.length + (title.length ? ' &nbsp; ' + title : '') + '</span>';
													},
				'transitionIn'	: 'fade',
				'transitionOut'	: 'fade'
			});
		});




	// autocomplete

	var suggestions = [
		{match: 'Turistično spremljanje', cat: 'dejavnost'},
		{match: 'Turistični spremljevalec', cat: 'poklic'},
		{match: 'Turistično vodenje', cat: 'dejavnost'},
		{match: 'Turistično vodenje', cat: 'dovoljenje'},
		{match: 'Turistično vodnik', cat: 'poklic'},
		{match: 'Turistično vodenje na turističnem območju', cat: 'poklic'},
		{match: 'Organiziranje turističnih aranžmajev', cat: 'dejavnost'},
		{match: 'Arhitekturno projektiranje', cat: 'poklic'}
	];

	if ($('#search').length || $('#content-search').length) {
		var suggestWidth = '527px';
		if ( $('#content-search').length) suggestWidth = '645px';
		if ( $('body.small-suggest').length) suggestWidth = '365px';
		$('#search,#content-search').autocomplete(suggestions,{
			minChars: 2,
			matchContains: "word",
			autoFill: true,
			width: suggestWidth,
			scrollHeight : '500px',
			formatItem: function(row) {
				return row.match + ' <span class="cat"><span>/</span> ' + row.cat + '</span>';
			},
			formatMatch: function(row, i, max) {
				return row.match;
			},
			formatResult: function(row) {
				return row.match;
			}
		});
	}

});
