package com.kishore.sb.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.kishore.sb.advice.Advisor;
import com.kishore.sb.model.Decision;

public class AdvisorUtil {

	private AdvisorUtil() {
	}

	public static Advisor combine(Collection<Advisor> advisors) {
		return new CombinedAdvisor(advisors);
	}

	private static class CombinedAdvisor implements Advisor {

		private final Collection<Advisor> advisors;

		public CombinedAdvisor(Collection<Advisor> advisors) {
			
			List<Advisor> advisorList = new ArrayList<>(advisors);

			Collections.sort(advisorList, (a, b) -> {
				return a.order() - b.order();
			});
			
			this.advisors = advisorList;
		}
		
		@Override
		public int order() {
			return 1000;
		}

		@Override
		public boolean advise(Decision decision) {
			if (advisors.isEmpty()) {
				return false;
			}
			for (final Advisor advisor : advisors) {
				if (!advisor.advise(decision)) {
					return false;
				}
			}
			return true;
		}



	}

}
