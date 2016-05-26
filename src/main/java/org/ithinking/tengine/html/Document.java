package org.ithinking.tengine.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ithinking.tengine.core.AbstractRender;
import org.ithinking.tengine.core.Context;

public class Document extends AbstractRender {

	private List<Tag> headers;
	private Map<String, Tag> fragments;

	@Override
	public void render(Context context) {
		this.writeHeaders(context).renderBody(context);
	}

	private Document writeHeaders(Context context) {
		if (headers != null) {
			for (Tag head : headers) {
				head.writeHeader(context);
			}
		}
		return this;
	}

	public Tag getFragment(String fragid) {
		if (fragments != null) {
			Tag tag = fragments.get(fragid);
			if(tag.getFragid().equals(fragid)){
				return tag;
			}
		}
		return null;
	}

	public void addHeader(Tag header) {
		if (headers == null) {
			headers = new ArrayList<Tag>();
		}
		headers.add(header);
	}

	public void addFragment(Tag frag) {
		if (fragments == null) {
			fragments = new HashMap<String, Tag>();
		}
		if (fragments.containsKey(frag.getFragid())) {
			// log...
		}
		fragments.put(frag.getFragid(), frag);
	}

}
