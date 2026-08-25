package com.driversfiles.www.core.dao.criteria;

import java.util.ArrayList;
import java.util.List;

public class JunctionCriterion implements Criterion {

	private static final long serialVersionUID = 1L;

	private final boolean conjunction;
	private final List<Criterion> criterions;

	JunctionCriterion(boolean conjunction, Criterion[] criterions) {
		this.conjunction = conjunction;
		this.criterions = new ArrayList<Criterion>();
		if (criterions != null) {
			for (Criterion c : criterions) {
				this.criterions.add(c);
			}
		}
	}

	public JunctionCriterion add(Criterion criterion) {
		criterions.add(criterion);
		return this;
	}

	public boolean isConjunction() { return conjunction; }
	public Criterion[] getCriterions() { return criterions.toArray(new Criterion[0]); }
}
