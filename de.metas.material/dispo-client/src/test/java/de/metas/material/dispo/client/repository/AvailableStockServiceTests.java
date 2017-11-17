package de.metas.material.dispo.client.repository;

import static de.metas.testsupport.MetasfreshAssertions.assertThatModel;
import static org.assertj.core.api.Assertions.assertThat;

import org.adempiere.mm.attributes.api.ImmutableAttributeSet;
import org.adempiere.mm.attributes.api.impl.AttributesTestHelper;
import org.adempiere.test.AdempiereTestHelper;
import org.adempiere.test.AdempiereTestWatcher;
import org.compiere.model.I_M_Attribute;
import org.compiere.model.X_M_Attribute;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;

import de.metas.material.dispo.commons.repository.StockRepository;
import de.metas.material.event.commons.ProductDescriptor;

/*
 * #%L
 * metasfresh-material-dispo-commons
 * %%
 * Copyright (C) 2017 metas GmbH
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

public class AvailableStockServiceTests
{
	/** Watches the current tests and dumps the database to console in case of failure */
	@Rule
	public final TestWatcher testWatcher = new AdempiereTestWatcher();

	private StockRepository stockRepository;

	private AttributesTestHelper attributesTestHelper;

	@Before
	public void init()
	{
		AdempiereTestHelper.get().init();
		stockRepository = new StockRepository();

		attributesTestHelper = new AttributesTestHelper();
	}

	@Test
	public void createAttributeSetFromStorageAttributesKey()
	{
		final I_M_Attribute attr1 = attributesTestHelper.createM_Attribute("attr1", X_M_Attribute.ATTRIBUTEVALUETYPE_List, true);
		attributesTestHelper.createM_AttributeValue(attr1, "value");

		final I_M_Attribute attr2 = attributesTestHelper.createM_Attribute("attr2", X_M_Attribute.ATTRIBUTEVALUETYPE_List, true);
		attributesTestHelper.createM_AttributeValue(attr2, "value2");

		// invoke the method under test
		final ImmutableAttributeSet result = new AvailableStockService(stockRepository)
				.createAttributeSetFromStorageAttributesKey("value" + ProductDescriptor.STORAGE_ATTRIBUTES_KEY_DELIMITER + "value2");

		assertThat(result.getAttributes()).hasSize(2);

		assertThat(result.getAttributes()).anySatisfy(attribute -> {
			assertThatModel(attribute).hasSameIdAs(attr1);
			assertThat(result.getValueAsString(attribute)).isEqualTo("value");
		});

		assertThat(result.getAttributes()).anySatisfy(attribute -> {
			assertThatModel(attribute).hasSameIdAs(attr2);
			assertThat(result.getValueAsString(attribute)).isEqualTo("value2");
		});


	}

}
