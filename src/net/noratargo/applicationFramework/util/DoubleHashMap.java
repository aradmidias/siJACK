package net.noratargo.applicationFramework.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DoubleHashMap<SuperKey, SubKey, Value> {

	private final Map<SuperKey, Map<SubKey, Value>> dhm;
	private final Set<Value> valueSet;

	public DoubleHashMap() {
		dhm = new HashMap<SuperKey, Map<SubKey, Value>>();
		valueSet = new HashSet<Value>();
	}

	/**
	 * Stores the given vlaue under the Super- and SubKeys. If an element, stored under the same Super- and Subkey is
	 * alredady present, it will be replaced.
	 * 
	 * @param sk
	 * @param k
	 * @param v
	 */
	public void put(SuperKey sk, SubKey k, Value v) {
		Map<SubKey, Value> subMap = dhm.get(sk);

		if (subMap == null) {
			subMap = new HashMap<SubKey, Value>();
			dhm.put(sk, subMap);
		}

		subMap.put(k, v);
		valueSet.remove(v);
		valueSet.add(v);
	}

	/**
	 * Checks, whether an item is stored under the given SuperKey, SubKey constellation.
	 * 
	 * @param sk
	 * @param k
	 * @return <code>true</code> if the item exists, <code>false</code> if not.
	 */
	public boolean contains(SuperKey sk, SubKey k) {
		Map<SubKey, Value> subMap = dhm.get(sk);

		if (subMap == null) {
			return false;
		}

		return subMap.containsKey(k);
	}

	/**
	 * Rturns the stored item. Can return <code>null</code> if thre is no item stored, or if <code>null</code> has been
	 * stored.
	 * 
	 * @param sk
	 * @param k
	 * @return
	 */
	public Value get(SuperKey sk, SubKey k) {
		Map<SubKey, Value> subMap = dhm.get(sk);

		if (subMap == null) {
			return null;
		}

		return subMap.get(k);
	}
	
	public Set<Value> getValueSet() {
		return Collections.unmodifiableSet(valueSet);
	}

	public boolean hasSuperKey(SuperKey sk) {
		return dhm.containsKey(sk);
	}
}
