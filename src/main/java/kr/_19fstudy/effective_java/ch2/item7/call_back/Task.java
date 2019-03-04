package kr._19fstudy.effective_java.ch2.item7.call_back;

import java.util.List;

public abstract class Task {
	public final void executeWith(List<CallBack> callBacks) {
		execute();
		if (callBacks != null && callBacks.size() != 0) {
			for (CallBack callBack : callBacks)
				callBack.call();
		}
	}

	public abstract void execute();
}
