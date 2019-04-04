import React from "react";


class Comment extends React.Component {

    constructor(props) {
        super(props);

        this.DeleteComment = this.DeleteComment.bind(this);
    }

    DeleteComment() {
        this.props.onDeleteComment(this.props.comment.id);
    }

    render() {
        return  (
            <div  className="animated fadeIn" >
                <li>
                    <div className="commenterImage">
                        <img src="https://ssl.gstatic.com/accounts/ui/avatar_2x.png" />
                    </div>
                    <div className="commentText">
                        <span className="date sub-text">{this.props.comment.date}</span>
                        {
                            this.props.token ? (
                                <a onClick={this.DeleteComment} className="btn-sm btn-info ml-5">Törlés</a>
                            ) : (
                                <div></div>
                            )
                        }
                        <p className="lead mb-5">{this.props.comment.content}</p>
                        
                    </div>
                    
                </li>
                <hr/>
            </div>
        )
    }

}

export default Comment;