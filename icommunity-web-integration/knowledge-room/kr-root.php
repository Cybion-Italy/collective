<?php 
/*
Template Name: Knowledge Room Root
*/

/* This file should be included in the root BuddyPress theme directory */

get_header() ?>

	<div id="content">
		<div class="padder">

		<?php do_action( 'bp_before_blog_page' ) ?>

		<div class="page" id="blog-page" role="main">

				<h2 class="pagetitle"><?php the_title(); ?></h2>
				
				<?php 
				include_once 'KnowledgeTemplateCybion.class.php';
								
				global $knowledge_template;
				$knowledge_template = new KnowledgeTemplateCybion();
				
				include_once 'kr-main.php'; 
				
				?>						

		</div><!-- .page -->

		<?php do_action( 'bp_after_blog_page' ) ?>

		</div><!-- .padder -->
	</div><!-- #content -->

	<?php get_sidebar() ?>

<?php get_footer(); ?>
