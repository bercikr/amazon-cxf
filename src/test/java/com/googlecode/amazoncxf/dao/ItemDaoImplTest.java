package com.googlecode.amazoncxf.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.CollectionUtils;

import com.amazon.webservices.awsecommerceservice.Item;
import com.googlecode.amazoncxf.util.ItemFormatter;

public class ItemDaoImplTest {

	private static Log log = LogFactory.getLog(ItemDaoImplTest.class);
	static private ItemDao itemDao;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext factory = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		itemDao = (ItemDao) factory.getBean("itemDao");
		log.debug(((ItemDaoImpl) itemDao)
				.getAmazonAssociatesWebServiceAccount());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLookup() throws Exception {
		Item item = itemDao.lookup("B000FQ9QVI");
		log.debug(ItemFormatter.formatItem(item));
	}

	@Test
	public void testLookupResponseGroups() throws Exception {
		// An empty list is perfectly valid.
		List<String> responseGroups = new ArrayList<String>();
		Item item = itemDao.lookup("B000FQ9QVI", responseGroups);
		assertNotNull(item);
		log.debug(ItemFormatter.formatItem(item));

		// Add a valid RG and an invalid one.
		responseGroups = CollectionUtils.arrayToList(new String[] { "Small",
				"XXX" });
		try {
			item = itemDao.lookup("B000FQ9QVI", responseGroups);
			fail();
		} catch (IllegalArgumentException iae) {
		}
		
		// No Images
		responseGroups = CollectionUtils.arrayToList(new String[] { "Small",
				"Offers" });
		item = itemDao.lookup("B000FQ9QVI", responseGroups);
		assertEquals("We shouldn't have any images", 0, item.getImageSets().size());
		assertNull(item.getSmallImage());
	}

	@Test
	public void testGetItems() throws Exception {
		log.debug("testGetItems");
		List<String> asins = new ArrayList<String>();
		asins.add("B000FQ9QVI");
		asins.add("B000VWYJ86");
		asins.add("B000TFINY6");
		asins.add("B000JLKIHA");
		asins.add("B0009RGLSE");
		asins.add("0060762012");
		asins.add("B0011TQLA2");
		asins.add("B000NNK4DM");
		asins.add("B000P297ES");
		asins.add("1932394885");
		List<Item> items = itemDao.getItems(asins);

		for (Item item : items) {
			log.debug(ItemFormatter.formatItem(item));
		}
		assertNotNull(items);
		assertEquals(items.size(), asins.size());

		log.debug("testGetItems-more");
		List<String> moreAsins = CollectionUtils.arrayToList(new String[] {
				"B001992NUQ", "B001ABN82K", "B001DSNF8C", "B001E0RZ3U",
				"B001E2D44W", "B001IVXI7C", "B001LF2WCC", "B001OQCV74",
				"B001PKHRTG", "B001QIVEVE" });

		items = itemDao.getItems(moreAsins);
		for (Item item : items) {
			log.debug(ItemFormatter.formatItem(item));
		}
		assertNotNull(items);
	}

	@Test
	public void testSearchItems() throws Exception {
		log.debug("testSearchItems");
		List<Item> items = itemDao.searchItems("ps3");

		assertNotNull(items);
		assertTrue(items.size() > 0);
		for (Item item : items) {
			log.debug(ItemFormatter.formatItem(item));
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetItemsTooMany() throws Exception {
		// Now try to do it with even more ASINS
		List<String> asins = new ArrayList<String>();
		asins.add("B000FQ9QVI");
		asins.add("B000VWYJ86");
		asins.add("B000TFINY6");
		asins.add("B000JLKIHA");
		asins.add("B0009RGLSE");
		asins.add("0060762012");
		asins.add("B0011TQLA2");
		asins.add("B000NNK4DM");
		asins.add("B000P297ES");
		asins.add("1932394885");
		asins.add("B000KICN7U");
		itemDao.getItems(asins);
	}
}
