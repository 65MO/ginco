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

package fr.mcc.ginco.services;

import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.dao.IThesaurusTermDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.solr.SearchResult;
import fr.mcc.ginco.solr.SearchResultList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Transactional(readOnly = true, rollbackFor = BusinessException.class)
@Service("thesaurusReportService")
public class ThesaurusReportServiceImpl implements IThesaurusReportService {

	@Inject
	private IThesaurusConceptDAO conceptDAO;

	@Inject
	private IThesaurusTermDAO termDAO;

	@Inject
	private IThesaurusConceptService thesaurusConceptService;

	@Override
	public SearchResultList getConceptsWithoutNotes(String thesaurus,
	                                                int startIndex, int limit) {
		SearchResultList resultList = new SearchResultList();
		List<ThesaurusConcept> concepts = conceptDAO.getConceptsWoNotes(thesaurus, startIndex, limit);
		for (ThesaurusConcept concept : concepts) {
			resultList.add(getSearchResultFromConcept(concept));
		}
		resultList.setNumFound(conceptDAO.countConceptsWoNotes(thesaurus));
		return resultList;
	}

	@Override
	public SearchResultList getConceptsAlignedToMyThes(String thesaurus,
	                                                   int startIndex, int limit) {
		SearchResultList resultList = new SearchResultList();
		List<ThesaurusConcept> concepts = conceptDAO.getConceptsAlignedToMyThes(thesaurus, startIndex, limit);
		for (ThesaurusConcept concept : concepts) {
			resultList.add(getSearchResultFromConcept(concept));
		}
		resultList.setNumFound(conceptDAO.countConceptsAlignedToMyThes(thesaurus));
		return resultList;
	}

	private SearchResult getSearchResultFromConcept(ThesaurusConcept concept) {
		SearchResult result = new SearchResult();
		result.setIdentifier(concept.getIdentifier());
		result.setType("ThesaurusConcept");
		try {
			String prefLabel = thesaurusConceptService.getConceptPreferredTerm(
					concept.getIdentifier()).getLexicalValue();
			result.setLexicalValue(prefLabel);
		} catch (BusinessException ex) {
			result.setLexicalValue("");
		}
		result.setThesaurusId(concept.getThesaurusId());
		return result;
	}

	@Override
	public SearchResultList getTermsWithoutNotes(String thesaurus,
	                                             int startIndex, int limit) {
		SearchResultList resultList = new SearchResultList();
		List<ThesaurusTerm> terms = termDAO.getTermsWoNotes(thesaurus, startIndex, limit);
		for (ThesaurusTerm term : terms) {
			resultList.add(getSearchResultFromTerm(term, thesaurus));
		}
		resultList.setNumFound(termDAO.countTermsWoNotes(thesaurus));
		return resultList;
	}

	private SearchResult getSearchResultFromTerm(ThesaurusTerm term,
	                                             String thesaurus) {
		SearchResult result = new SearchResult();
		result.setIdentifier(term.getIdentifier());
		result.setType("ThesaurusTerm");
		result.setLexicalValue(term.getLexicalValue());
		result.setThesaurusId(thesaurus);
		return result;
	}

}
