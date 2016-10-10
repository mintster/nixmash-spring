package com.nixmash.springdata.solr.utils;

import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.solr.dto.ProductDTO;
import com.nixmash.springdata.solr.model.IProduct;
import com.nixmash.springdata.solr.model.PostDoc;
import com.nixmash.springdata.solr.model.Product;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daveburke on 9/30/16.
 */
public class SolrUtils {


    // region Products

    public static ProductDTO productToProductDTO(Product product) {
        ProductDTO dto = new ProductDTO();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setWeight(product.getWeight());
        dto.setPrice(product.getPrice());
        dto.setPopularity(product.getPopularity());
        dto.setAvailable(product.isAvailable());
        dto.setDoctype(product.getDoctype());
        dto.setCategories(product.getCategories());
        dto.setLocation(product.getLocation());
        dto.setPoint(product.getPoint());
        return dto;
    }

    public static HighlightPage<Product> processHighlights(HighlightPage<Product> productPage) {
        int i = 0;
        for (HighlightEntry<Product> product : productPage.getHighlighted()) {
            for (HighlightEntry.Highlight highlight : product.getHighlights()) {
                for (String snippet : highlight.getSnipplets()) {
                    if (highlight.getField().getName().equals(IProduct.NAME_FIELD)) {
                        productPage.getContent().get(i).setName(snippet);
                    }
                }
            }
            i++;
        }
        return productPage;
    }

    public static List<Product> highlightPagesToList(HighlightPage<Product> productPage) {

        List<Product> products = new ArrayList<Product>();
        for (HighlightEntry<Product> highlightedProduct : productPage.getHighlighted()) {

            Product product = new
                    Product(highlightedProduct.getEntity().getId(), highlightedProduct.getEntity().getName());
            products.add(product);

            for (HighlightEntry.Highlight highlight : highlightedProduct.getHighlights()) {
                for (String snippet : highlight.getSnipplets()) {
                    if (highlight.getField().getName().equals(IProduct.NAME_FIELD)) {
                        product.setName(snippet);
                    }
                }
            }
        }
        return products;
    }

    // endregion

    // region Posts

    public static PostDoc createPostDoc(Post post) {
        PostDoc postDoc = PostDoc.getBuilder(post.getPostId(),
                post.getPostTitle(),
                post.getAuthorFullname(),
                post.getPostName(),
                post.getPostLink(),
                post.getPostContent(),
                post.getPostSource(),
                post.getPostType().name())
                .postDate(post.getPostDate())
                .tags(post.tags).build();
        return postDoc;
    }

    // endregion


    // region display content

    public static void printPostDocs(List<PostDoc> postDocs) {
        for (PostDoc postDoc :
                postDocs) {
            System.out.println(postDoc.getPostTitle()
                    + "\n" + postDoc.getPostText() + " : " + postDoc.getPostType() + "\n------------------------");
        }
    }

    // endregion
}
