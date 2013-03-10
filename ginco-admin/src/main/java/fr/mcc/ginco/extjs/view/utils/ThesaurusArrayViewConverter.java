/**
 * Copyright or © or Copr. Ministère Français chargé de la Culture
 * et de la Communication (2013)
 * <p/>
 * contact.gincoculture_at_gouv.fr
 * <p/>
 * This software is a computer program whose purpose is to provide a thesaurus
 * management solution.
 * <p/>
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software. You can use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * <p/>
 * As a counterpart to the access to the source code and rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty and the software's author, the holder of the
 * economic rights, and the successive licensors have only limited liability.
 * <p/>
 * In this respect, the user's attention is drawn to the risks associated
 * with loading, using, modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean that it is complicated to manipulate, and that also
 * therefore means that it is reserved for developers and experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systemsand/or
 * data to be ensured and, more generally, to use and operate it in the
 * same conditions as regards security.
 * <p/>
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.mcc.ginco.extjs.view.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusArrayView;
import fr.mcc.ginco.services.INodeLabelService;
import fr.mcc.ginco.services.IThesaurusArrayService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusService;

@Component("thesaurusArrayViewConverter")
public class ThesaurusArrayViewConverter {

	@Value("${ginco.default.language}")
	private String language;

	@Inject
	@Named("thesaurusService")
	private IThesaurusService thesaurusService;
	
	@Inject
	@Named("thesaurusArrayService")
	private IThesaurusArrayService thesaurusArrayService;

	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Inject
	@Named("thesaurusConceptViewConverter")
	private ThesaurusConceptViewConverter thesaurusConceptViewConverter;

	@Inject
	@Named("nodeLabelService")
	private INodeLabelService nodeLabelService;

	@Inject
	@Named("nodeLabelViewConverter")
	private NodeLabelViewConverter nodeLabelViewConverter;

	public ThesaurusArray convert(ThesaurusArrayView source)
			throws BusinessException {
		ThesaurusArray hibernateRes;
		if (StringUtils.isEmpty(source.getIdentifier())) {
			hibernateRes = new ThesaurusArray();
		} else {
			hibernateRes = thesaurusArrayService.getThesaurusArrayById(source
					.getIdentifier());
		}
		if ("".equals(source.getThesaurusId())) {
			throw new BusinessException(
					"ThesaurusId is mandatory to save a concept",
					"mandatory-thesaurus");
		} else {
			Thesaurus thesaurus = new Thesaurus();
			thesaurus = thesaurusService.getThesaurusById(source
					.getThesaurusId());
			hibernateRes.setThesaurus(thesaurus);
		}
		
		if (StringUtils.isEmpty(source.getSuperOrdinateId())) {
			throw new BusinessException(
					"ThesaurusArray must have superordirnated concept!",
					"array-should-have-superordirnated-concept");
		}

		if (StringUtils.isNotEmpty(source.getSuperOrdinateId())) {
			hibernateRes
					.setSuperOrdinateConcept(thesaurusConceptService
							.getThesaurusConceptById(source
									.getSuperOrdinateId()));
		}

		if (hibernateRes.getConcepts() == null) {
			hibernateRes.setConcepts(new HashSet<ThesaurusConcept>());
		}
		hibernateRes.getConcepts().clear();

		for (String conceptId : source
				.getConcepts()) {
			ThesaurusConcept concept = thesaurusConceptService
					.getThesaurusConceptById(conceptId);
			if (concept == null) {
				throw new BusinessException("Concept doest not exist",
						"concept-does-not-exist");
			}
			hibernateRes.getConcepts().add(concept);
		}
		
		hibernateRes.setOrdered(false);

		return hibernateRes;
	}

	public ThesaurusArrayView convert(final ThesaurusArray source)
			throws BusinessException {
		ThesaurusArrayView thesaurusArrayView = new ThesaurusArrayView();

		thesaurusArrayView.setIdentifier(source.getIdentifier());

		if (source.getSuperOrdinateConcept() != null) {
			thesaurusArrayView.setSuperOrdinateId(source
					.getSuperOrdinateConcept().getIdentifier());
			thesaurusArrayView
					.setSuperOrdinateLabel(thesaurusConceptService
							.getConceptLabel(source.getSuperOrdinateConcept()
									.getIdentifier()));

		}

		List<String> conceptsIds = new ArrayList<String>();
		for (ThesaurusConcept concept: source.getConcepts()) {
			conceptsIds.add(concept.getIdentifier());
		}
		thesaurusArrayView
				.setConcepts(conceptsIds);

		NodeLabel label = nodeLabelService
				.getByThesaurusArrayAndLanguage(source.getIdentifier());

		thesaurusArrayView.setLabel(label.getLexicalValue());
		thesaurusArrayView.setLanguage(label.getLanguage().getId());
		thesaurusArrayView.setNodeLabelId(label.getIdentifier());

		thesaurusArrayView.setThesaurusId(source.getThesaurus()
				.getThesaurusId());

		return thesaurusArrayView;
	}
}