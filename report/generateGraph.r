require(reshape2)
require(ggplot2)

/* different ghosts */

ggplot(data = melt(df), aes(x=variable, y=value)) + xlab("") + ylab("Score") + geom_boxplot(aes(fill=variable)) + theme(legend.position="none")

/* training data */

ggplot(data=training2[c(1:600),], aes(x=Generation, y=AvgFitness)) + geom_step() + xlab("Generation") + ylab("AvgFitness")
